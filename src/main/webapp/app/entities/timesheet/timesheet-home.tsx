/* eslint-disable no-console */
import React, { useState, useEffect } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Translate } from 'react-jhipster';
import { useNavigate } from 'react-router-dom';
import { getEntities, getEntitiesByUser } from 'app/entities/timesheet/timesheet.reducer';
import './timesheet-home.css';
import { TimesheetEntryModal } from './timesheet-entry-modal';
import 'react-datepicker/dist/react-datepicker.css';
import { Calendar, momentLocalizer } from 'react-big-calendar';
import 'react-big-calendar/lib/css/react-big-calendar.css';

import moment from 'moment';
moment.updateLocale('pt-br', {
  week: {
    dow: 0, // Domingo é o primeiro dia da semana
    doy: 6, // A primeira semana do ano deve conter 1º de janeiro
  },
});

// Definindo interfaces para melhorar a clareza do código
interface TimeEntry {
  id?: string | number;
  date: string; // Data no formato ISO ou similar
  totalHours: number; // Total de horas trabalhadas
  overtimeHours: number | null; // Horas extras, possivelmente nulas
  user?: {
    id: string | number;
  };
  company?: {
    id: string | number;
  };
  status?: string;
}

interface PeriodSummary {
  total: number;
  overtime: number;
}

interface TimesheetSummary {
  month: PeriodSummary;
  week: PeriodSummary;
  day: PeriodSummary;
}

export const TimesheetHome = () => {
  const navigate = useNavigate();

  const dispatch = useAppDispatch();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedTimesheetId, setSelectedTimesheetId] = useState<string | number | null>(null);
  const localizer = momentLocalizer(moment);
  const account = useAppSelector(state => state.authentication.account);
  const [selectedDate, setSelectedDate] = useState(new Date());

  // Modificado para incluir um mapa de datas com entradas
  const [calendarData, setCalendarData] = useState({});

  // Data from Redux
  const timeEntities = useAppSelector(state => state.timesheet.entities);
  const loading = useAppSelector(state => state.timesheet.loading);
  const updateSuccess = useAppSelector(state => state.timesheet.updateSuccess);

  // Função para converter timeEntities para eventos de calendário
  const getCalendarEvents = () => {
    if (!timeEntities || timeEntities.length === 0) {
      return [];
    }

    return timeEntities.map(entry => {
      const startDate = moment(entry.date, 'YYYY-MM-DD HH:mm').toDate();
      const endDate = moment(entry.date, 'YYYY-MM-DD HH:mm').toDate();

      if (entry.user.id === account.id) {
        return {
          id: entry.id,
          title: `${entry.totalHours}h - ${entry.company.id || 'Sem descrição'}`,
          start: startDate,
          end: endDate,
          resource: entry, // Guarda o objeto original para uso posterior
        };
      } else {
        return null;
      }
    });
  };

  // Definindo tipos para os eventos do calendário
  type CalendarEvent = {
    id: string | number;
    title: string;
    start: Date;
    end: Date;
    allDay?: boolean;
    resource?: any;
  };

  // Função para formatar data para comparação
  const formatDateKey = date => {
    return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;
  };

  // Manipulador para quando o usuário clica em um evento
  const handleEventClick = event => {
    // Abre o modal de edição com o ID da entrada clicada
    setSelectedTimesheetId(event.id);
    setIsModalOpen(true);
  };
  // Manipulador para quando o usuário clica em um slot vazio
  const handleSlotSelect = slotInfo => {
    // Abre o modal para criar nova entrada na data selecionada
    setSelectedTimesheetId(null);
    setIsModalOpen(true);
    setNewEntryDate(slotInfo.start);
  };

  // Estado para armazenar a data selecionada para nova entrada
  const [newEntryDate, setNewEntryDate] = useState(null);

  // Customize o componente de evento para mostrar informações adicionais
  const EventComponent = ({ event }) => {
    const timesheet = event.resource;
    const status = timesheet.status;

    // Defina classes CSS com base no status
    let statusClass = '';
    switch (status.toLowerCase()) {
      case 'aprovado':
      case 'approved':
        statusClass = 'event-approved';
        break;
      case 'pendente':
      case 'pending':
        statusClass = 'event-pending';
        break;
      case 'rejeitado':
      case 'rejected':
        statusClass = 'event-rejected';
        break;
      default:
        statusClass = 'event-draft';
    }

    return (
      <div className={`calendar-event ${statusClass}`}>
        <div className="event-title">{timesheet.totalHours}</div>
        <div className="event-detail">
          {timesheet.modality && (
            <span className="event-modality">
              {(() => {
                switch (timesheet.modality) {
                  case 'IN_PERSON':
                    return 'Presencial';
                  case 'REMOTE':
                    return 'Remoto';
                  case 'HYBRID':
                    return 'Hibrido';
                  default:
                    '';
                }
              })()}
            </span>
          )}
          <span className="event-description">{timesheet.company.id}</span>

          {timesheet.description && (
            <span className="event-description">
              {timesheet.description.substring(0, 30)}
              {timesheet.description.length > 30 ? '...' : ''}
            </span>
          )}
        </div>
      </div>
    );
  };

  // Summary data
  const [summary, setSummary] = useState({
    month: { total: 0, overtime: 0 },
    week: { total: 0, overtime: 0 },
    day: { total: 0, overtime: 0 },
  });

  // // Função para lidar com clique em uma data do calendário
  // const handleCalendarClick = (date) => {
  //   const dateKey = formatDateKey(date);
  //   const entriesForDate = calendarData[dateKey] || [];
  //
  //   if (entriesForDate.length > 0) {
  //     // Se já existir uma entrada para esta data, abra o modal de edição
  //     setSelectedTimesheetId(entriesForDate[0].id);
  //     setIsModalOpen(true);
  //   } else {
  //     // Se não existir entrada, abra o modal para criar uma nova
  //     setSelectedTimesheetId(null);
  //     // Pré-seleciona a data clicada para o novo registro
  //     setIsModalOpen(true);
  //     // Aqui você precisará passar a data selecionada para o modal de alguma forma
  //     // Por exemplo, você pode adicionar um estado para armazenar a data selecionada
  //     // e passá-la para o modal
  //
  //     // setNewEntryDate(date);
  //   }
  // };

  // // Renderiza conteúdo personalizado para cada data no calendário
  // const renderDayContents = (day, date) => {
  //   const dateKey = formatDateKey(date);
  //   const entriesForDay = calendarData[dateKey] || [];
  //   const hasEntries = entriesForDay.length > 0;
  //
  //   // Calcule o total de horas para esta data
  //   const totalHours = entriesForDay.reduce((sum, entry) => sum + entry.totalHours, 0);
  //
  //   return (
  //     <div className={`calendar-day ${hasEntries ? 'has-entries' : ''}`}>
  //       <span className="day-number">{day}</span>
  //       {hasEntries && (
  //         <div className="entry-info">
  //           <span className="entry-hours">{totalHours}h</span>
  //         </div>
  //       )}
  //     </div>
  //   );
  // };

  useEffect(() => {
    if (timeEntities && timeEntities.length > 0) {
      const entriesByDate = {};

      timeEntities.forEach(entry => {
        const dateKey = formatDateKey(new Date(entry.date));

        if (!entriesByDate[dateKey]) {
          entriesByDate[dateKey] = [];
        }

        entriesByDate[dateKey].push(entry);
      });

      setCalendarData(entriesByDate);
    }
  }, [timeEntities]);

  // Load data on component mount and after updates
  useEffect(() => {
    if (!account || !account.id) {
      navigate('/login');
      return;
    }

    if (account && account.id) {
      dispatch(
        getEntitiesByUser({
          userId: account.id,
          page: 0,
          size: 999,
        }),
      );
    }
  }, [account, dispatch]);

  // Reload data after successful update
  useEffect(() => {
    if (updateSuccess) {
      dispatch(
        getEntitiesByUser({
          userId: account.id,
          page: 0,
          size: 999,
        }),
      );
    }
  }, [updateSuccess]);

  // Calculate summary when timeEntities change
  useEffect(() => {
    if (timeEntities && timeEntities.length > 0) {
      // calculateSummary();
      updateTimesheetSummary();
    }
  }, [timeEntities]);

  const handleDateChange = date => {
    setSelectedDate(date);
    // Aqui você pode adicionar lógica para filtrar os timeEntries com base na data selecionada
  };

  // // Calculate summary data
  // const calculateSummary = (): void => {
  //   const now = moment();
  //
  //   // Extrair funções para filtrar entradas por período
  //   const filterEntriesByPeriod = (filterFn: (entryDate: moment.Moment) => boolean) =>
  //     timeEntities.filter(entry => filterFn(moment(entry.date)));
  //
  //   // Filtrar entradas por diferentes períodos usando moment.js
  //   const monthEntries = filterEntriesByPeriod(entryDate => entryDate.month() === now.month() && entryDate.year() === now.year());
  //
  //   // Calcular início da semana (segunda-feira)
  //   const startOfWeek = moment(now).startOf('week').add(1, 'days');
  //   if (startOfWeek.isAfter(now)) {
  //     startOfWeek.subtract(7, 'days');
  //   }
  //
  //   const weekEntries = filterEntriesByPeriod(
  //     entryDate => entryDate.isSameOrAfter(startOfWeek, 'day') && entryDate.isSameOrBefore(now, 'day'),
  //   );
  //
  //   const dayEntries = filterEntriesByPeriod(entryDate => entryDate.isSame(now, 'day'));
  //
  //   // Calcular totais por período
  //   const calculatePeriodTotals = (entries: typeof timeEntities) => {
  //     const periodTotals = calculateTotals(entries);
  //     return periodTotals;
  //   };
  //
  //   // Atualizar o estado do resumo
  //   setSummary({
  //     month: calculatePeriodTotals(monthEntries),
  //     week: calculatePeriodTotals(weekEntries),
  //     day: calculatePeriodTotals(dayEntries),
  //   });
  // };
  //
  // // Helper to calculate total and overtime hours
  // const calculateTotals = entries => {
  //   const total = entries.reduce((sum, entry) => sum + entry.totalHours, 0);
  //   const overtime = entries.reduce((sum, entry) => sum + entry.overtimeHours, 0);
  //
  //   return {
  //     total: parseFloat(total.toFixed(2)),
  //     overtime: parseFloat(overtime.toFixed(2)),
  //   };
  // };

  /**
   * Calcula os totais de horas normais e extras para um conjunto de entradas
   * @param entries - Lista de entradas de tempo
   * @returns Objeto com totais formatados de horas normais e extras
   */
  /**
   * Calcula os totais de horas normais e extras para um conjunto de entradas
   * @param entries - Lista de entradas de tempo com base na regra: mais de 8h por dia = hora extra
   * @returns Objeto com totais formatados de horas normais e extras
   */
  const calculatePeriodTotals = (entries: TimeEntry[]): PeriodSummary => {
    // Verificação inicial para evitar processamento desnecessário
    if (!entries || entries.length === 0) {
      return { total: 0, overtime: 0 };
    }

    // Agrupa entradas por data para calcular horas extras corretamente
    const entriesByDate: Record<string, TimeEntry[]> = {};

    // Agrupa entradas pela mesma data
    entries.forEach(entry => {
      if (!entry.date) return; // Pula entradas sem data

      // Formato ISO da data (YYYY-MM-DD) sem o componente de tempo
      const dateKey = entry.date.substring(0, 10);

      if (!entriesByDate[dateKey]) {
        entriesByDate[dateKey] = [];
      }
      entriesByDate[dateKey].push(entry);
    });

    // Inicializa os acumuladores
    let totalHours = 0;
    let overtimeHours = 0;

    // Processa cada dia separadamente
    Object.values(entriesByDate).forEach(dailyEntries => {
      // Total de horas trabalhadas neste dia
      const dailyTotal = dailyEntries.reduce((sum, entry) => sum + (typeof entry.totalHours === 'number' ? entry.totalHours : 0), 0);

      // Adiciona ao total geral
      totalHours += dailyTotal;

      // Se trabalhou mais de 8 horas, calcula as horas extras
      if (dailyTotal > 8) {
        overtimeHours += dailyTotal - 8;
      }
    });

    // Retorna os totais formatados com precisão de 2 casas decimais
    return {
      total: parseFloat(totalHours.toFixed(2)),
      overtime: parseFloat(overtimeHours.toFixed(2)),
    };
  };

  // Função principal que define o resumo
  const updateTimesheetSummary = (): void => {
    const now = moment();

    // Extrair funções para filtrar entradas por período
    const filterEntriesByPeriod = (filterFn: (entryDate: moment.Moment) => boolean) =>
      timeEntities.filter(entry => filterFn(moment(entry.date)));

    // Filtrar entradas por diferentes períodos usando moment.js
    const monthEntries = filterEntriesByPeriod(entryDate => entryDate.month() === now.month() && entryDate.year() === now.year());

    // Calcular início da semana (segunda-feira)
    const startOfWeek = moment(now).startOf('week').add(1, 'days');
    if (startOfWeek.isAfter(now)) {
      startOfWeek.subtract(7, 'days');
    }

    const weekEntries = filterEntriesByPeriod(
      entryDate => entryDate.isSameOrAfter(startOfWeek, 'day') && entryDate.isSameOrBefore(now, 'day'),
    );

    const dayEntries = filterEntriesByPeriod(entryDate => entryDate.isSame(now, 'day'));

    setSummary({
      month: calculatePeriodTotals(monthEntries),
      week: calculatePeriodTotals(weekEntries),
      day: calculatePeriodTotals(dayEntries),
    });
  };

  // Get the last 5 entries
  const recentEntries = timeEntities
    ? [...timeEntities].sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime()).slice(0, 5)
    : [];

  // Format date for display
  const formatDate = dateString => {
    const date = new Date(dateString);
    return date.toLocaleDateString();
  };

  // Open modal for creating a new timesheet
  const openCreateModal = () => {
    setSelectedTimesheetId(null);
    setIsModalOpen(true);
  };

  // Open modal for editing an existing timesheet
  const openEditModal = id => {
    setSelectedTimesheetId(id);
    setIsModalOpen(true);
  };

  // Close modal
  const closeModal = () => {
    setIsModalOpen(false);
    setSelectedTimesheetId(null);
  };

  return (
    <div className="timesheet-home-container">
      <div className="timesheet-header">
        <h1 className="page-title">
          <Translate contentKey="timesheetSystemApp.timesheet.home.title">Controle de Ponto</Translate>
        </h1>
        <div className="user-info">
          <p>
            {/* {account?.login || 'Não autenticado'} */}
            {account?.login && account?.lastName && ` ${account.firstName} ${account.lastName}`}
          </p>
        </div>

        <button className="btn-primary" onClick={openCreateModal}>
          <Translate contentKey="timesheetSystemApp.timesheet.home.registerTime">Registrar Ponto</Translate>
        </button>
      </div>

      {/* Time summary section */}
      <div className="summary-section">
        <h2 className="section-title">
          <Translate contentKey="timesheetSystemApp.timesheet.summary">Resumo de Horas</Translate>
        </h2>

        <div className="summary-cards">
          <div className="summary-card">
            <div className="summary-card-title">
              <Translate contentKey="timesheetSystemApp.timesheet.month">Mês Atual</Translate>
            </div>
            <div className="summary-card-content">
              <div className="summary-card-value">{summary.month.total}h</div>
              <div className="summary-card-subtitle">
                <Translate contentKey="timesheetSystemApp.timesheet.overtime">Extras:</Translate> {summary.month.overtime}h
              </div>
            </div>
          </div>

          <div className="summary-card">
            <div className="summary-card-title">
              <Translate contentKey="timesheetSystemApp.timesheet.week">Semana Atual</Translate>
            </div>
            <div className="summary-card-content">
              <div className="summary-card-value">{summary.week.total}h</div>
              <div className="summary-card-subtitle">
                <Translate contentKey="timesheetSystemApp.timesheet.overtime">Extras:</Translate> {summary.week.overtime}h
              </div>
            </div>
          </div>

          <div className="summary-card">
            <div className="summary-card-title">
              <Translate contentKey="timesheetSystemApp.timesheet.today">Hoje</Translate>
            </div>
            <div className="summary-card-content">
              <div className="summary-card-value">{summary.day.total}h</div>
              <div
                className={`summary-card-subtitle ${summary.day.overtime > 0 ? 'has-overtime' : ''}`}
                title={`${summary.day.overtime} horas extras registradas hoje`}
                aria-label={`Horas extras: ${summary.day.overtime} horas`}
              >
                <div className="summary-card-subtitle">
                  <Translate contentKey="timesheetSystemApp.timesheet.overtime">Extras:</Translate> {summary.day.overtime}h
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      {/* Seção de calendário estilo agenda */}
      <div className="timesheet-agenda-section">
        <h3>
          <Translate contentKey="timesheetHome.calendar">Agenda de Registros</Translate>
        </h3>

        <div className="calendar-legend">
          <div className="legend-item">
            <span className="legend-color event-approved"></span>
            <span>
              <Translate contentKey="timesheetHome.calendarLegend.approved">Aprovado</Translate>
            </span>
          </div>
          <div className="legend-item">
            <span className="legend-color event-pending"></span>
            <span>
              <Translate contentKey="timesheetHome.calendarLegend.pending">Pendente</Translate>
            </span>
          </div>
          <div className="legend-item">
            <span className="legend-color event-rejected"></span>
            <span>
              <Translate contentKey="timesheetHome.calendarLegend.rejected">Rejeitado</Translate>
            </span>
          </div>
          {/* <div className="legend-item"> */}
          {/*   <span className="legend-color event-draft"></span> */}
          {/*   <span> */}
          {/*     <Translate contentKey="timesheetHome.calendarLegend.draft">Rascunho</Translate> */}
          {/*   </span> */}
          {/* </div> */}
        </div>

        <div className="agenda-calendar-container">
          <Calendar
            localizer={localizer}
            events={getCalendarEvents()}
            startAccessor="start"
            endAccessor="end"
            style={{ height: 500 }}
            views={['month', 'week', 'day']}
            defaultView="month"
            onSelectEvent={handleEventClick}
            onSelectSlot={handleSlotSelect}
            selectable={true}
            dayLayoutAlgorithm="no-overlap"
            // components={{
            //   event: EventComponent,
            // }}

            messages={{
              today: 'Hoje',
              previous: 'Anterior',
              next: 'Próximo',
              month: 'Mês',
              week: 'Semana',
              day: 'Dia',
              agenda: 'Agenda',
              date: 'Data',
              time: 'Hora',
              event: 'Evento',
              noEventsInRange: 'Não há registros neste período.',
            }}
          />
        </div>
      </div>

      {/* Entry Modal */}
      {isModalOpen && (
        <TimesheetEntryModal
          isOpen={isModalOpen}
          onClose={() => setIsModalOpen(false)}
          timesheetId={selectedTimesheetId}
          selectedDate={selectedTimesheetId ? null : newEntryDate}
        />
      )}
    </div>
  );
};

export default TimesheetHome;
