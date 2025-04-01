import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Row, Col, Card, CardHeader, CardBody, Button, Form, FormGroup, Label, Input } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getTimesheets } from 'app/entities/timesheet/timesheet.reducer';
import { getEntities as getCompanies } from 'app/entities/company/company.reducer';
import { LineChart, Line, BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, TooltipProps } from 'recharts';
import { ValueType, NameType } from 'recharts/types/component/DefaultTooltipContent';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

import './timesheet-dashboard.scss';

interface ChartDataPoint {
  date: string;
  regularHours: number;
  overtimeHours: number;
  totalHours: number;
  allowance: number;
}

export const TimesheetDashboard = () => {
  const dispatch = useAppDispatch();

  const [dateFilter, setDateFilter] = useState<{ startDate: string; endDate: string }>({
    startDate: new Date(new Date().setDate(1)).toISOString().split('T')[0], // Primeiro dia do mês atual
    endDate: new Date().toISOString().split('T')[0], // Hoje
  });

  const [companyFilter, setCompanyFilter] = useState<string>('all');
  const [modalityFilter, setModalityFilter] = useState<string>('all');
  const [chartData, setChartData] = useState<ChartDataPoint[]>([]);
  const [summaryData, setSummaryData] = useState<{
    totalHours: number;
    totalOvertimeHours: number;
    totalDays: number;
    averageHoursPerDay: number;
    totalAllowance: number;
  }>({
    totalHours: 0,
    totalOvertimeHours: 0,
    totalDays: 0,
    averageHoursPerDay: 0,
    totalAllowance: 0,
  });

  const timesheets = useAppSelector(state => state.timesheet.entities);
  const companies = useAppSelector(state => state.company.entities);
  const loading = useAppSelector(state => state.timesheet.loading);

  useEffect(() => {
    dispatch(getTimesheets({}));
    dispatch(getCompanies({}));
  }, []);

  // Processar dados quando os timesheets ou filtros mudarem
  useEffect(() => {
    if (timesheets && timesheets.length) {
      processTimesheetData();
    }
  }, [timesheets, dateFilter, companyFilter, modalityFilter]);

  // Função para processar dados dos timesheets para os gráficos
  const processTimesheetData = () => {
    // Filtrar timesheets por data, empresa e modalidade
    const filteredTimesheets = timesheets.filter(timesheet => {
      const meetDateCriteria =
        (!dateFilter.startDate || timesheet.date >= dateFilter.startDate) && (!dateFilter.endDate || timesheet.date <= dateFilter.endDate);

      const meetCompanyCriteria = companyFilter === 'all' || (timesheet.company && timesheet.company.id.toString() === companyFilter);

      const meetModalityCriteria = modalityFilter === 'all' || timesheet.modality === modalityFilter;

      return meetDateCriteria && meetCompanyCriteria && meetModalityCriteria;
    });

    // Agrupar por data para o gráfico
    const dataByDate: Record<string, ChartDataPoint> = filteredTimesheets.reduce(
      (acc, timesheet) => {
        const date = timesheet.date;
        if (!acc[date]) {
          acc[date] = {
            date,
            regularHours: 0,
            overtimeHours: 0,
            totalHours: 0,
            allowance: 0,
          };
        }

        acc[date].totalHours += timesheet.totalHours || 0;
        acc[date].overtimeHours += timesheet.overtimeHours || 0;
        acc[date].regularHours += (timesheet.totalHours || 0) - (timesheet.overtimeHours || 0);
        acc[date].allowance += timesheet.allowanceValue || 0;

        return acc;
      },
      {} as Record<string, ChartDataPoint>,
    );

    // Converter para array e ordenar por data
    const chartDataArray: ChartDataPoint[] = Object.values(dataByDate).sort(
      (a, b) => new Date(a.date).getTime() - new Date(b.date).getTime(),
    );

    setChartData(chartDataArray);

    // Calcular dados de resumo
    const totalHours = filteredTimesheets.reduce((sum, ts) => sum + (ts.totalHours || 0), 0);
    const totalOvertimeHours = filteredTimesheets.reduce((sum, ts) => sum + (ts.overtimeHours || 0), 0);
    const totalDays = Object.keys(dataByDate).length;
    const totalAllowance = filteredTimesheets.reduce((sum, ts) => sum + (ts.allowanceValue || 0), 0);

    setSummaryData({
      totalHours: Number(totalHours.toFixed(2)),
      totalOvertimeHours: Number(totalOvertimeHours.toFixed(2)),
      totalDays,
      averageHoursPerDay: totalDays ? Number((totalHours / totalDays).toFixed(2)) : 0,
      totalAllowance: Number(totalAllowance.toFixed(2)),
    });
  };

  // Formatters para o tooltip
  const hoursFormatter = (value: ValueType, name?: string) => {
    if (typeof value === 'number') {
      return `${value} h`;
    }
    return value;
  };

  const currencyFormatter = (value: ValueType, name?: string) => {
    if (typeof value === 'number') {
      return `R$ ${value.toFixed(2)}`;
    }
    return value;
  };

  // Função para lidar com mudanças nos filtros
  const handleFilterChange = event => {
    const { name, value } = event.target;

    if (name === 'startDate' || name === 'endDate') {
      setDateFilter(prev => ({ ...prev, [name]: value }));
    } else if (name === 'company') {
      setCompanyFilter(value);
    } else if (name === 'modality') {
      setModalityFilter(value);
    }
  };

  // Função para exportar relatório
  const exportReport = () => {
    // Lógica para exportar relatório
    alert('Funcionalidade de exportação será implementada em breve!');
  };

  // Função para renderizar os gráficos
  const renderCharts = () => {
    if (chartData.length === 0) {
      return (
        <div className="alert alert-info mt-3">
          <Translate contentKey="timesheetSystemApp.dashboard.noData">Nenhum dado encontrado para os filtros selecionados.</Translate>
        </div>
      );
    }

    return (
      <Row>
        <Col md={12} className="mb-4">
          <Card>
            <CardHeader>
              <h5 className="m-0">
                <Translate contentKey="timesheetSystemApp.dashboard.hoursChart">Horas Trabalhadas por Dia</Translate>
              </h5>
            </CardHeader>
            <CardBody>
              <ResponsiveContainer width="100%" height={300}>
                <BarChart data={chartData} margin={{ top: 5, right: 30, left: 20, bottom: 5 }}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="date" />
                  <YAxis />
                  <Tooltip formatter={hoursFormatter} />
                  <Legend />
                  <Bar dataKey="regularHours" name="Horas Regulares" fill="#8884d8" stackId="a" />
                  <Bar dataKey="overtimeHours" name="Horas Extras" fill="#82ca9d" stackId="a" />
                </BarChart>
              </ResponsiveContainer>
            </CardBody>
          </Card>
        </Col>

        <Col md={6}>
          <Card>
            <CardHeader>
              <h5 className="m-0">
                <Translate contentKey="timesheetSystemApp.dashboard.hoursEvolution">Evolução de Horas</Translate>
              </h5>
            </CardHeader>
            <CardBody>
              <ResponsiveContainer width="100%" height={250}>
                <LineChart data={chartData} margin={{ top: 5, right: 30, left: 20, bottom: 5 }}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="date" />
                  <YAxis />
                  <Tooltip formatter={hoursFormatter} />
                  <Legend />
                  <Line type="monotone" dataKey="totalHours" name="Total de Horas" stroke="#8884d8" activeDot={{ r: 8 }} />
                </LineChart>
              </ResponsiveContainer>
            </CardBody>
          </Card>
        </Col>

        <Col md={6}>
          <Card>
            <CardHeader>
              <h5 className="m-0">
                <Translate contentKey="timesheetSystemApp.dashboard.allowanceChart">Ajuda de Custo</Translate>
              </h5>
            </CardHeader>
            <CardBody>
              <ResponsiveContainer width="100%" height={250}>
                <LineChart data={chartData} margin={{ top: 5, right: 30, left: 20, bottom: 5 }}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="date" />
                  <YAxis />
                  <Tooltip formatter={currencyFormatter} />
                  <Legend />
                  <Line type="monotone" dataKey="allowance" name="Ajuda de Custo" stroke="#82ca9d" />
                </LineChart>
              </ResponsiveContainer>
            </CardBody>
          </Card>
        </Col>
      </Row>
    );
  };

  // Função para renderizar o resumo
  const renderSummary = () => {
    return (
      <Card className="mb-4">
        <CardHeader>
          <h5 className="m-0">
            <Translate contentKey="timesheetSystemApp.dashboard.summary">Resumo</Translate>
          </h5>
        </CardHeader>
        <CardBody>
          <Row>
            <Col md={2} className="text-center">
              <div className="summary-box">
                <h6>
                  <Translate contentKey="timesheetSystemApp.dashboard.totalDays">Total de Dias</Translate>
                </h6>
                <div className="summary-value">{summaryData.totalDays}</div>
              </div>
            </Col>
            <Col md={2} className="text-center">
              <div className="summary-box">
                <h6>
                  <Translate contentKey="timesheetSystemApp.dashboard.totalHours">Total de Horas</Translate>
                </h6>
                <div className="summary-value">{summaryData.totalHours} h</div>
              </div>
            </Col>
            <Col md={2} className="text-center">
              <div className="summary-box">
                <h6>
                  <Translate contentKey="timesheetSystemApp.dashboard.overtimeHours">Horas Extras</Translate>
                </h6>
                <div className="summary-value">{summaryData.totalOvertimeHours} h</div>
              </div>
            </Col>
            <Col md={3} className="text-center">
              <div className="summary-box">
                <h6>
                  <Translate contentKey="timesheetSystemApp.dashboard.averageHours">Média Diária</Translate>
                </h6>
                <div className="summary-value">{summaryData.averageHoursPerDay} h</div>
              </div>
            </Col>
            <Col md={3} className="text-center">
              <div className="summary-box">
                <h6>
                  <Translate contentKey="timesheetSystemApp.dashboard.totalAllowance">Ajuda de Custo</Translate>
                </h6>
                <div className="summary-value">R$ {summaryData.totalAllowance.toFixed(2)}</div>
              </div>
            </Col>
          </Row>
        </CardBody>
      </Card>
    );
  };

  return (
    <div className="timesheet-dashboard-container">
      <h2 id="timesheet-dashboard-heading">
        <Translate contentKey="timesheetSystemApp.dashboard.title">Dashboard de Ponto</Translate>
      </h2>

      {/* Filtros */}
      <Card className="mb-4">
        <CardHeader>
          <h5 className="m-0">
            <FontAwesomeIcon icon="filter" /> <Translate contentKey="timesheetSystemApp.dashboard.filters">Filtros</Translate>
          </h5>
        </CardHeader>
        <CardBody>
          <Form>
            <Row>
              <Col md={3}>
                <FormGroup>
                  <Label for="startDate">
                    <Translate contentKey="timesheetSystemApp.dashboard.startDate">Data Inicial</Translate>
                  </Label>
                  <Input type="date" name="startDate" id="startDate" value={dateFilter.startDate} onChange={handleFilterChange} />
                </FormGroup>
              </Col>
              <Col md={3}>
                <FormGroup>
                  <Label for="endDate">
                    <Translate contentKey="timesheetSystemApp.dashboard.endDate">Data Final</Translate>
                  </Label>
                  <Input type="date" name="endDate" id="endDate" value={dateFilter.endDate} onChange={handleFilterChange} />
                </FormGroup>
              </Col>
              <Col md={3}>
                <FormGroup>
                  <Label for="company">
                    <Translate contentKey="timesheetSystemApp.dashboard.company">Empresa</Translate>
                  </Label>
                  <Input type="select" name="company" id="company" value={companyFilter} onChange={handleFilterChange}>
                    <option value="all">Todas</option>
                    {companies.map(company => (
                      <option key={company.id} value={company.id.toString()}>
                        {company.name}
                      </option>
                    ))}
                  </Input>
                </FormGroup>
              </Col>
              <Col md={3}>
                <FormGroup>
                  <Label for="modality">
                    <Translate contentKey="timesheetSystemApp.dashboard.modality">Modalidade</Translate>
                  </Label>
                  <Input type="select" name="modality" id="modality" value={modalityFilter} onChange={handleFilterChange}>
                    <option value="all">Todas</option>
                    <option value="REMOTE">Remoto</option>
                    <option value="IN_PERSON">Presencial</option>
                    <option value="HYBRID">Híbrido</option>
                  </Input>
                </FormGroup>
              </Col>
            </Row>
            <Row>
              <Col className="text-right">
                <Button color="primary" onClick={exportReport}>
                  <FontAwesomeIcon icon="file-export" />{' '}
                  <Translate contentKey="timesheetSystemApp.dashboard.exportReport">Exportar Relatório</Translate>
                </Button>
              </Col>
            </Row>
          </Form>
        </CardBody>
      </Card>

      {/* Resumo */}
      {renderSummary()}

      {/* Gráficos */}
      {loading ? (
        <div className="text-center">
          <FontAwesomeIcon icon="spinner" spin />{' '}
          <Translate contentKey="timesheetSystemApp.dashboard.loading">Carregando dados...</Translate>
        </div>
      ) : (
        renderCharts()
      )}
    </div>
  );
};

export default TimesheetDashboard;
