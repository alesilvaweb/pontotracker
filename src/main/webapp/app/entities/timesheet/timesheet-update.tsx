/* eslint-disable no-console */
import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Translate } from 'react-jhipster';
import { getEntity, updateEntity, createEntity, reset } from 'app/entities/timesheet/timesheet.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getCompanies } from 'app/entities/company/company.reducer';

import './timesheet-update.css'; // Create this CSS file for custom styles

// Interface for time entry
interface TimeEntry {
  id?: number;
  startTime: string;
  endTime: string;
  type: string;
  hoursWorked: number;
  description: string;
}

// Component for time entry inputs
const TimeEntryInput = ({
  entry,
  index,
  onChange,
  onRemove,
  disabled,
  entriesCount,
}: {
  entry: TimeEntry;
  index: number;
  onChange: (index: number, field: string, value: string | number) => void;
  onRemove: (index: number) => void;
  disabled?: boolean;
  entriesCount: number;
}) => (
  <div className="time-entry-card">
    <div className="time-entry-grid">
      <div className="time-entry-field">
        <label htmlFor={`startTime-${index}`}>
          <Translate contentKey="timesheetSystemApp.timeEntry.startTime">Hora Inicial</Translate>
        </label>
        <input
          id={`startTime-${index}`}
          type="time"
          value={entry.startTime}
          onChange={e => onChange(index, 'startTime', e.target.value)}
          required
          disabled={disabled}
          step="300"
        />
      </div>
      <div className="time-entry-field">
        <label htmlFor={`endTime-${index}`}>
          <Translate contentKey="timesheetSystemApp.timeEntry.endTime">Hora Final</Translate>
        </label>
        <input
          id={`endTime-${index}`}
          type="time"
          value={entry.endTime}
          onChange={e => onChange(index, 'endTime', e.target.value)}
          required
          disabled={disabled}
          step="300"
        />
      </div>
      <div className="time-entry-field">
        <label htmlFor={`type-${index}`}>
          <Translate contentKey="timesheetSystemApp.timeEntry.type">Tipo</Translate>
        </label>
        <select id={`type-${index}`} value={entry.type} onChange={e => onChange(index, 'type', e.target.value)} disabled={disabled}>
          <option value="REGULAR">
            <Translate contentKey="timesheetSystemApp.EntryType.REGULAR">Regular</Translate>
          </option>
          <option value="OVERTIME">
            <Translate contentKey="timesheetSystemApp.EntryType.OVERTIME">Hora Extra</Translate>
          </option>
          <option value="BREAK">
            <Translate contentKey="timesheetSystemApp.EntryType.BREAK">Intervalo</Translate>
          </option>
          <option value="LUNCH">
            <Translate contentKey="timesheetSystemApp.EntryType.LUNCH">Almoço</Translate>
          </option>
        </select>
      </div>
      <div className="time-entry-field">
        <label htmlFor={`hoursWorked-${index}`}>
          <Translate contentKey="timesheetSystemApp.timeEntry.hoursWorked">Horas</Translate>
        </label>
        <input id={`hoursWorked-${index}`} type="number" value={entry.hoursWorked} step="0.1" min="0" readOnly disabled />
      </div>
      <div className="time-entry-button">
        {!disabled && entriesCount > 1 && (
          <button type="button" className="btn-delete" onClick={() => onRemove(index)} disabled={disabled}>
            ✕
          </button>
        )}
      </div>
    </div>
  </div>
);

export const TimesheetUpdate = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  // State for form inputs and calculations
  const [timeEntries, setTimeEntries] = useState<TimeEntry[]>([
    { startTime: '09:00', endTime: '12:00', type: 'REGULAR', hoursWorked: 3.0, description: '' },
  ]);
  const [formState, setFormState] = useState({
    date: new Date().toISOString().split('T')[0],
    modality: 'REMOTE',
    classification: '',
    description: '',
    userId: '',
    companyId: '',
  });
  const [totalHours, setTotalHours] = useState(0);
  const [overtimeHours, setOvertimeHours] = useState(0);
  const [formErrors, setFormErrors] = useState({
    date: '',
    userId: '',
    companyId: '',
  });

  // Selectors for data from Redux store
  const users = useAppSelector(state => state.userManagement.users);
  const companies = useAppSelector(state => state.company.entities);
  const timesheetEntity = useAppSelector(state => state.timesheet.entity);
  const loading = useAppSelector(state => state.timesheet.loading);
  const updating = useAppSelector(state => state.timesheet.updating);
  const updateSuccess = useAppSelector(state => state.timesheet.updateSuccess);

  // Load data on component mount
  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getCompanies({}));
  }, []);

  // Update form state when entity data is loaded
  useEffect(() => {
    if (timesheetEntity && timesheetEntity.id) {
      setFormState({
        date: timesheetEntity.date || new Date().toISOString().split('T')[0],
        modality: timesheetEntity.modality || 'REMOTE',
        classification: timesheetEntity.classification || '',
        description: timesheetEntity.description || '',
        userId: timesheetEntity.user?.id.toString() || '',
        companyId: timesheetEntity.company?.id.toString() || '',
      });

      if (timesheetEntity.timeEntries) {
        // Map time entries from entity
        const mappedEntries = timesheetEntity.timeEntries.map(entry => ({
          id: entry.id,
          startTime: entry.startTime ? new Date(entry.startTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : '',
          endTime: entry.endTime ? new Date(entry.endTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : '',
          type: entry.type,
          hoursWorked: entry.hoursWorked,
          description: entry.description || '',
        }));

        setTimeEntries(
          mappedEntries.length > 0
            ? mappedEntries
            : [{ startTime: '09:00', endTime: '12:00', type: 'REGULAR', hoursWorked: 3.0, description: '' }],
        );
        setTotalHours(timesheetEntity.totalHours || 0);
        setOvertimeHours(timesheetEntity.overtimeHours || 0);
      }
    }
  }, [timesheetEntity]);

  // Calculate hours based on start and end time
  const calculateHours = (startTime: string, endTime: string): number => {
    if (!startTime || !endTime) return 0;

    const [startHour, startMinute] = startTime.split(':').map(Number);
    const [endHour, endMinute] = endTime.split(':').map(Number);

    let hours = endHour - startHour;
    let minutes = endMinute - startMinute;

    if (minutes < 0) {
      hours -= 1;
      minutes += 60;
    }

    return parseFloat((hours + minutes / 60).toFixed(2));
  };

  // Recalculate totals when time entries change
  useEffect(() => {
    const newTotalHours = timeEntries.reduce((total, entry) => {
      if (entry.type === 'BREAK' || entry.type === 'LUNCH') return total;
      return total + calculateHours(entry.startTime, entry.endTime);
    }, 0);

    setTotalHours(parseFloat(newTotalHours.toFixed(2)));

    // Calculate overtime hours (considering 8h as standard workday)
    const newOvertimeHours = Math.max(0, newTotalHours - 8);
    setOvertimeHours(parseFloat(newOvertimeHours.toFixed(2)));

    // Update hours worked for each entry
    const updatedEntries = timeEntries.map(entry => ({
      ...entry,
      hoursWorked: calculateHours(entry.startTime, entry.endTime),
    }));

    setTimeEntries(updatedEntries);
  }, [timeEntries.map(e => e.startTime + e.endTime + e.type).join(',')]);

  // Add new time entry
  const addTimeEntry = () => {
    setTimeEntries([...timeEntries, { startTime: '13:00', endTime: '17:00', type: 'REGULAR', hoursWorked: 4.0, description: '' }]);
  };

  // Remove time entry
  const removeTimeEntry = (index: number) => {
    if (timeEntries.length > 1) {
      setTimeEntries(timeEntries.filter((_, i) => i !== index));
    }
  };

  // Update time entry field
  const updateTimeEntry = (index: number, field: string, value: string | number) => {
    const updatedEntries = [...timeEntries];
    updatedEntries[index] = {
      ...updatedEntries[index],
      [field]: value,
    };
    setTimeEntries(updatedEntries);
  };

  // Form field change handler
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormState({
      ...formState,
      [name]: value,
    });

    // Clear validation error when field is modified
    if (formErrors[name]) {
      setFormErrors({
        ...formErrors,
        [name]: '',
      });
    }
  };

  // Form validation
  const validateForm = (): boolean => {
    const errors = {
      date: '',
      userId: '',
      companyId: '',
    };
    let isValid = true;

    if (!formState.date) {
      errors.date = 'Data é obrigatória';
      isValid = false;
    }

    if (!formState.userId) {
      errors.userId = 'Funcionário é obrigatório';
      isValid = false;
    }

    if (!formState.companyId) {
      errors.companyId = 'Empresa é obrigatória';
      isValid = false;
    }

    setFormErrors(errors);
    return isValid;
  };

  // Form submission handler
  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!validateForm()) {
      return;
    }

    const entity = {
      ...timesheetEntity,
      date: formState.date,
      modality: formState.modality,
      classification: formState.classification,
      description: formState.description,
      timeEntries: timeEntries.map(entry => ({
        id: entry.id,
        startTime: `${formState.date}T${entry.startTime}:00Z`,
        endTime: `${formState.date}T${entry.endTime}:00Z`,
        type: entry.type,
        hoursWorked: entry.hoursWorked,
        description: entry.description,
      })),
      totalHours,
      overtimeHours,
      user: users.find(it => it.id.toString() === formState.userId),
      company: companies.find(it => it.id.toString() === formState.companyId),
      status: 'PENDING',
      createdAt: new Date().toISOString(),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  // Navigate after save
  useEffect(() => {
    if (updateSuccess) {
      navigate('/timesheet');
    }
  }, [updateSuccess]);

  return (
    <div className="container">
      <h1 className="page-title">
        <Translate contentKey="timesheetSystemApp.timesheet.home.createOrEditLabel">Criar ou Editar Registro de Ponto</Translate>
      </h1>

      {loading ? (
        <div className="loading-container">
          <div className="loading-spinner"></div>
        </div>
      ) : (
        <form onSubmit={handleSubmit}>
          <div className="card">
            <div className="form-grid">
              <div className="form-group">
                <label htmlFor="date">
                  <Translate contentKey="timesheetSystemApp.timesheet.date">Data</Translate>
                </label>
                <input
                  id="date"
                  name="date"
                  type="date"
                  value={formState.date}
                  onChange={handleInputChange}
                  required
                  className={formErrors.date ? 'error' : ''}
                />
                {formErrors.date && <div className="error-message">{formErrors.date}</div>}
              </div>

              <div className="form-group">
                <label htmlFor="modality">
                  <Translate contentKey="timesheetSystemApp.timesheet.modality">Modalidade</Translate>
                </label>
                <select id="modality" name="modality" value={formState.modality} onChange={handleInputChange}>
                  <option value="REMOTE">
                    <Translate contentKey="timesheetSystemApp.WorkModality.REMOTE">Remoto</Translate>
                  </option>
                  <option value="IN_PERSON">
                    <Translate contentKey="timesheetSystemApp.WorkModality.IN_PERSON">Presencial</Translate>
                  </option>
                  <option value="HYBRID">
                    <Translate contentKey="timesheetSystemApp.WorkModality.HYBRID">Híbrido</Translate>
                  </option>
                </select>
              </div>

              <div className="form-group full-width">
                <label htmlFor="classification">
                  <Translate contentKey="timesheetSystemApp.timesheet.classification">Classificação</Translate>
                </label>
                <input
                  id="classification"
                  name="classification"
                  type="text"
                  value={formState.classification}
                  onChange={handleInputChange}
                />
              </div>

              <div className="form-group full-width">
                <label htmlFor="description">
                  <Translate contentKey="timesheetSystemApp.timesheet.description">Descrição</Translate>
                </label>
                <textarea id="description" name="description" value={formState.description} onChange={handleInputChange} rows={3} />
              </div>

              <div className="form-group">
                <label htmlFor="userId">
                  <Translate contentKey="timesheetSystemApp.timesheet.user">Funcionário</Translate>
                </label>
                <select
                  id="userId"
                  name="userId"
                  value={formState.userId}
                  onChange={handleInputChange}
                  required
                  className={formErrors.userId ? 'error' : ''}
                >
                  <option value="">
                    <Translate contentKey="entity.select.prompt">Selecione</Translate>
                  </option>
                  {users.map(user => (
                    <option key={user.id} value={user.id.toString()}>
                      {user.firstName} {user.lastName} ({user.login})
                    </option>
                  ))}
                </select>
                {formErrors.userId && <div className="error-message">{formErrors.userId}</div>}
              </div>

              <div className="form-group">
                <label htmlFor="companyId">
                  <Translate contentKey="timesheetSystemApp.timesheet.company">Empresa</Translate>
                </label>
                <select
                  id="companyId"
                  name="companyId"
                  value={formState.companyId}
                  onChange={handleInputChange}
                  required
                  className={formErrors.companyId ? 'error' : ''}
                >
                  <option value="">
                    <Translate contentKey="entity.select.prompt">Selecione</Translate>
                  </option>
                  {companies.map(company => (
                    <option key={company.id} value={company.id.toString()}>
                      {company.name}
                    </option>
                  ))}
                </select>
                {formErrors.companyId && <div className="error-message">{formErrors.companyId}</div>}
              </div>
            </div>
          </div>

          {/* Time entries section */}
          <div className="card">
            <div className="card-header">
              <h2 className="card-title">
                <Translate contentKey="timesheetSystemApp.timeEntry.home.title">Registros de Horário</Translate>
              </h2>
            </div>
            <div className="card-content">
              {timeEntries.map((entry, index) => (
                <TimeEntryInput
                  key={index}
                  entry={entry}
                  index={index}
                  onChange={updateTimeEntry}
                  onRemove={removeTimeEntry}
                  disabled={updating}
                  entriesCount={timeEntries.length}
                />
              ))}

              <div className="button-center">
                <button type="button" className="btn-outline" onClick={addTimeEntry} disabled={updating}>
                  + <Translate contentKey="timesheetSystemApp.timeEntry.addEntry">Adicionar Entrada</Translate>
                </button>
              </div>
            </div>
          </div>

          {/* Summary section */}
          <div className="card summary-card">
            <h2 className="card-title">
              <Translate contentKey="timesheetSystemApp.timesheet.summary">Resumo</Translate>
            </h2>
            <div className="summary-grid">
              <div className="summary-item">
                <div className="summary-label">
                  <Translate contentKey="timesheetSystemApp.timesheet.totalHours">Total de Horas</Translate>
                </div>
                <div className="summary-value primary">{totalHours} h</div>
              </div>
              <div className="summary-item">
                <div className="summary-label">
                  <Translate contentKey="timesheetSystemApp.timesheet.overtimeHours">Horas Extras</Translate>
                </div>
                <div className="summary-value secondary">{overtimeHours} h</div>
              </div>
            </div>
          </div>

          {/* Form actions */}
          <div className="button-container">
            <Link to="/timesheet" className="btn-outline">
              <Translate contentKey="entity.action.back">Voltar</Translate>
            </Link>

            <button type="submit" className="btn-primary" disabled={updating}>
              <Translate contentKey="entity.action.save">Salvar</Translate>
            </button>
          </div>
        </form>
      )}
    </div>
  );
};

export default TimesheetUpdate;
