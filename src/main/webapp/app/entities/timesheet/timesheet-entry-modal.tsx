/* eslint-disable no-console */
import React, { useState, useEffect } from 'react';
import { Modal, Button, Form, Row, Col } from 'react-bootstrap';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Translate } from 'react-jhipster';
import { createEntity, getEntity, updateEntity } from 'app/entities/timesheet/timesheet.reducer';
import { getEntities as getCompanies } from 'app/entities/company/company.reducer';
import dayjs from 'dayjs';
import { toast } from 'react-toastify';
import { getEntities as getTimeEntry, getTimeEntriesByTimesheet } from 'app/entities/time-entry/time-entry.reducer';
import { updateEntity as updateTimeEntry, createEntity as createTimeEntry, reset } from 'app/entities/time-entry/time-entry.reducer';

// Definição de tipos de enum
type WorkModality = 'REMOTE' | 'IN_PERSON' | 'HYBRID';
type EntryType = 'REGULAR' | 'OVERTIME' | 'BREAK' | 'LUNCH';
type TimesheetStatus = 'PENDING' | 'APPROVED' | 'REJECTED';

interface FormTimeEntry {
  id?: number;
  startTime: string;
  endTime: string;
  type: EntryType;
  hoursWorked: number;
  description: string;
}

interface TimesheetFormState {
  date: string;
  modality: WorkModality;
  description: string;
  companyId: string;
}

export interface TimesheetEntryModalProps {
  isOpen: boolean;
  onClose: () => void;
  timesheetId: string | number | null;
  selectedDate?: Date;
}

export const TimesheetEntryModal = ({ isOpen, onClose, timesheetId, selectedDate }: TimesheetEntryModalProps) => {
  const dispatch = useAppDispatch();
  const isEditMode = !!timesheetId;

  // Obter o usuário logado
  const account = useAppSelector(state => state.authentication.account);
  const companies = useAppSelector(state => state.company.entities);
  const timesheetEntity = useAppSelector(state => state.timesheet.entity);
  const loading = useAppSelector(state => state.timesheet.loading);
  const updating = useAppSelector(state => state.timesheet.updating);
  const updateSuccess = useAppSelector(state => state.timesheet.updateSuccess);
  const timeEntryEntity = useAppSelector(state => state.timeEntry.entity);
  const timeEntry = useAppSelector(state => state.timeEntry.entities);

  // State for form data
  const [formState, setFormState] = useState<TimesheetFormState>({
    date: selectedDate ? dayjs(selectedDate).format('YYYY-MM-DD') : dayjs().format('YYYY-MM-DD'),
    modality: 'REMOTE',
    description: '',
    companyId: '',
  });

  // State for time entries
  const [timeEntries, setTimeEntries] = useState<FormTimeEntry[]>([
    { startTime: '07:30', endTime: '12:00', type: 'REGULAR', hoursWorked: 4.5, description: '' },
    { startTime: '13:30', endTime: '17:00', type: 'REGULAR', hoursWorked: 3.5, description: '' },
  ]);

  const calculateHoursWorked = (startTime: string, endTime: string): number => {
    if (!startTime || !endTime) return 0;

    const start = dayjs(`2000-01-01 ${startTime}`);
    const end = dayjs(`2000-01-01 ${endTime}`);

    // Se end for menor que start, significa que passou para o próximo dia
    const diff = end.isBefore(start) ? end.add(1, 'day').diff(start, 'minute') : end.diff(start, 'minute');

    return Number((diff / 60).toFixed(2));
  };

  useEffect(() => {
    if (isEditMode) {
      dispatch(getEntity(timesheetId));
      dispatch(getTimeEntriesByTimesheet(timesheetId)).then(
        value => {
          console.log('Time entries carregadas:', value.payload['data']);
        },
        error => {
          console.error('Erro ao carregar time entries:', error);
        },
      );
    }
  }, [dispatch, isEditMode, timesheetId]);

  // Preencher o formulário com os dados do timesheet existente quando em modo de edição
  useEffect(() => {
    if (isEditMode && timesheetEntity && timesheetEntity.id) {
      console.log('Carregando timesheet para edição:', timesheetEntity);

      // Formato da data para input type date (YYYY-MM-DD)
      const date = timesheetEntity.date ? dayjs(timesheetEntity.date).format('YYYY-MM-DD') : dayjs().format('YYYY-MM-DD');

      setFormState({
        date,
        modality: timesheetEntity.modality || 'REMOTE',
        description: timesheetEntity.description || '',
        companyId: timesheetEntity.company?.id.toString() || '',
      });

      // Mapear entradas de tempo existentes
      if (isEditMode && timeEntry && timeEntry.length > 0) {
        const mappedEntries = timeEntry.map(entry => {
          let startTime = '';
          let endTime = '';

          // Formatando os horários
          if (entry.startTime) {
            try {
              startTime = dayjs(entry.startTime).format('HH:mm');
            } catch (error) {
              console.error('Erro ao formatar startTime:', error);
              startTime = '';
            }
          }

          if (entry.endTime) {
            try {
              endTime = dayjs(entry.endTime).format('HH:mm');
            } catch (error) {
              console.error('Erro ao formatar endTime:', error);
              endTime = '';
            }
          }

          // Calculando as horas trabalhadas (ou usando o valor existente)
          const hoursWorked = entry.hoursWorked || calculateHoursWorked(startTime, endTime);

          return {
            id: entry.id,
            startTime,
            endTime,
            type: entry.type || 'REGULAR',
            hoursWorked,
            description: entry.description || '',
          };
        });

        console.log('TimeEntries mapeadas para o formulário:', mappedEntries);

        // Só atualiza o estado se tivermos entradas válidas
        if (mappedEntries.length > 0) {
          setTimeEntries(mappedEntries);
        }
      }
    } else if (selectedDate && !isEditMode) {
      // Se não estiver editando mas tiver uma data selecionada do calendário
      setFormState(prevState => ({
        ...prevState,
        date: dayjs(selectedDate).format('YYYY-MM-DD'),
      }));
    }
  }, [isEditMode, timesheetEntity, selectedDate]);

  // Load required data
  useEffect(() => {
    dispatch(getCompanies({}));

    // Se estiver em modo de edição, buscar os dados do timesheet
    if (isEditMode) {
      dispatch(getEntity(timesheetId));
    }
  }, [dispatch, isEditMode, timesheetId]);

  // Redirecionar após sucesso
  useEffect(() => {
    if (updateSuccess) {
      onClose();
    }
  }, [updateSuccess, isEditMode, onClose]);

  // Form field change handler
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormState(prev => ({
      ...prev,
      [name]: value,
    }));
  };

  // Form field change handler for modality (typed)
  const handleModalityChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const value = e.target.value as WorkModality;
    setFormState(prev => ({
      ...prev,
      modality: value,
    }));
  };

  // Time entry change handler
  const handleTimeEntryChange = (index: number, field: keyof FormTimeEntry, value: any) => {
    setTimeEntries(prevEntries => {
      const newEntries = [...prevEntries];
      newEntries[index] = { ...newEntries[index], [field]: value };

      // Calcular hoursWorked se startTime e endTime estiverem presentes
      if (field === 'startTime' || field === 'endTime') {
        const { startTime, endTime } = newEntries[index];
        if (startTime && endTime) {
          const start = dayjs(`2000-01-01T${startTime}`);
          const end = dayjs(`2000-01-01T${endTime}`);
          const diffHours = end.diff(start, 'minute') / 60;
          newEntries[index].hoursWorked = Math.max(0, parseFloat(diffHours.toFixed(2)));
        }
      }
      return newEntries;
    });
  };

  // Add new time entry
  const addTimeEntry = () => {
    setTimeEntries([...timeEntries, { startTime: '', endTime: '', type: 'REGULAR', hoursWorked: 0, description: '' }]);
  };

  // Remove time entry
  const removeTimeEntry = (index: number) => {
    setTimeEntries(timeEntries.filter((_, i) => i !== index));
  };

  // Calculate total hours
  const totalHours = timeEntries.reduce((sum, entry) => sum + entry.hoursWorked, 0);

  // Reset form when modal closes and opens again
  useEffect(() => {
    if (!isOpen) {
      // Reset form quando o modal é fechado
      setFormState({
        date: selectedDate ? dayjs(selectedDate).format('YYYY-MM-DD') : dayjs().format('YYYY-MM-DD'),
        modality: 'REMOTE',
        description: '',
        companyId: '',
      });
      setTimeEntries([{ startTime: '07:30', endTime: '12:00', type: 'REGULAR', hoursWorked: 4.0, description: '' }]);
    }
  }, [isOpen, selectedDate]);

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();

    // Log para debug
    console.log('TimeEntries antes do submit:', timeEntries);

    // Transform form data into the format expected by the API
    const entity = {
      id: isEditMode ? timesheetEntity?.id : undefined,
      date: dayjs(formState.date),
      modality: formState.modality,
      description: formState.description,
      company: {
        id: parseInt(formState.companyId, 10),
      },
      totalHours,
      createdBy: account?.login,
      status: isEditMode ? timesheetEntity?.status : 'APPROVED',
      createdAt: isEditMode ? timesheetEntity?.createdAt : dayjs(),
      user: {
        id: account?.id,
        login: account?.login,
      },
    };

    console.log('Dados formatados para envio:', entity);

    if (isEditMode) {
      dispatch(updateEntity(entity)).then(value => {
        if (value.meta.requestStatus === 'fulfilled') {
          timeEntries.map(entry => {
            // Cria objetos dayjs para os horários
            const startDateTime = dayjs(`${formState.date}T${entry.startTime}`);
            const endDateTime = dayjs(`${formState.date}T${entry.endTime}`);

            console.log(`Convertendo timeEntry:
        Data: ${formState.date}
        Start: ${entry.startTime} -> ${startDateTime.format()}
        End: ${entry.endTime} -> ${endDateTime.format()}`);

            dispatch(
              updateTimeEntry({
                id: entry.id,
                startTime: startDateTime,
                endTime: endDateTime,
                type: entry.type,
                hoursWorked: entry.hoursWorked,
                description: entry.description,
                timesheet: value.payload['data'],
              }),
            );
          });
        }
      });
    } else {
      dispatch(createEntity(entity)).then(value => {
        timeEntries.map(entry => {
          // Cria objetos dayjs para os horários
          const startDateTime = dayjs(`${formState.date}T${entry.startTime}`);
          const endDateTime = dayjs(`${formState.date}T${entry.endTime}`);

          console.log(`Convertendo timeEntry:
        Data: ${formState.date}
        Start: ${entry.startTime} -> ${startDateTime.format()}
        End: ${entry.endTime} -> ${endDateTime.format()}`);

          dispatch(
            createTimeEntry({
              startTime: startDateTime,
              endTime: endDateTime,
              type: entry.type,
              hoursWorked: entry.hoursWorked,
              description: entry.description,
              timesheet: value.payload['data'],
            }),
          );
        });
      });
    }
  };

  return (
    <Modal show={isOpen} onHide={onClose} backdrop="static" size="lg" centered aria-labelledby="timesheet-modal-title">
      <Form onSubmit={handleSubmit}>
        <Modal.Header closeButton>
          <Modal.Title id="timesheet-modal-title">
            <Translate contentKey={isEditMode ? 'timesheetHome.editEntry' : 'timesheetHome.newEntry'}>
              {isEditMode ? 'Editar Registro' : 'Novo Registro'}
            </Translate>
          </Modal.Title>
        </Modal.Header>

        <Modal.Body>
          {loading ? (
            <div className="d-flex justify-content-center">
              <div className="spinner-border text-primary" role="status">
                <span className="visually-hidden">Carregando...</span>
              </div>
            </div>
          ) : (
            <>
              <Row className="mb-3">
                <Col md={6}>
                  <Form.Group controlId="date">
                    <Form.Label>
                      <Translate contentKey="timesheetSystemApp.timesheet.date">Data</Translate>
                      <span className="text-danger">*</span>
                    </Form.Label>
                    <Form.Control type="date" name="date" value={formState.date} onChange={handleInputChange} required />
                  </Form.Group>
                </Col>
                <Col md={6}>
                  <Form.Group controlId="modality">
                    <Form.Label>
                      <Translate contentKey="timesheetSystemApp.timesheet.modality">Modalidade</Translate>
                    </Form.Label>
                    <Form.Select name="modality" value={formState.modality} onChange={handleModalityChange}>
                      <option value="REMOTE">Remoto</option>
                      <option value="IN_PERSON">Presencial</option>
                      <option value="HYBRID">Híbrido</option>
                    </Form.Select>
                  </Form.Group>
                </Col>
              </Row>

              <Row className="mb-3">
                <Col>
                  <Form.Group controlId="companyId">
                    <Form.Label>
                      <Translate contentKey="timesheetSystemApp.timesheet.company">Empresa</Translate>
                      <span className="text-danger">*</span>
                    </Form.Label>
                    <Form.Select name="companyId" value={formState.companyId} onChange={handleInputChange} required>
                      <option value="">Selecione...</option>
                      {companies.map(company => (
                        <option key={company.id} value={company.id}>
                          {company.name}
                        </option>
                      ))}
                    </Form.Select>
                  </Form.Group>
                </Col>
              </Row>

              <Row className="mb-3">
                <Col>
                  <Form.Group controlId="description">
                    <Form.Label>
                      <Translate contentKey="timesheetSystemApp.timesheet.description">Descrição</Translate>
                      {/* <span className="text-danger">*</span> */}
                    </Form.Label>
                    <Form.Control as="textarea" name="description" rows={2} value={formState.description} onChange={handleInputChange} />
                  </Form.Group>
                </Col>
              </Row>

              <h5 className="mt-4 mb-3">Registros de Tempo</h5>

              {timeEntries.map((entry, index) => (
                <div key={index} className="time-entry-row mb-3 p-3 border rounded">
                  <Row className="mb-2">
                    <Col md={5}>
                      <Form.Group controlId={`startTime-${index}`}>
                        <Form.Label>Hora Início</Form.Label>
                        <Form.Control
                          type="time"
                          value={entry.startTime}
                          onChange={e => handleTimeEntryChange(index, 'startTime', e.target.value)}
                          required
                        />
                      </Form.Group>
                    </Col>
                    <Col md={5}>
                      <Form.Group controlId={`endTime-${index}`}>
                        <Form.Label>Hora Fim</Form.Label>
                        <Form.Control
                          type="time"
                          value={entry.endTime}
                          onChange={e => handleTimeEntryChange(index, 'endTime', e.target.value)}
                          required
                        />
                      </Form.Group>
                    </Col>
                    <Col md={2}>
                      <Form.Group controlId={`hoursWorked-${index}`}>
                        <Form.Label>Horas</Form.Label>
                        <Form.Control type="text" value={entry.hoursWorked} readOnly />
                      </Form.Group>
                    </Col>
                  </Row>

                  <Row className="mb-2">
                    <Col md={4}>
                      <Form.Group controlId={`type-${index}`}>
                        <Form.Label>Tipo</Form.Label>
                        <Form.Select value={entry.type} onChange={e => handleTimeEntryChange(index, 'type', e.target.value as EntryType)}>
                          <option value="REGULAR">Regular</option>
                          <option value="OVERTIME">Hora Extra</option>
                          <option value="BREAK">Pausa</option>
                          <option value="LUNCH">Almoço</option>
                        </Form.Select>
                      </Form.Group>
                    </Col>
                    <Col md={8}>
                      <Form.Group controlId={`entryDescription-${index}`}>
                        <Form.Label>Descrição</Form.Label>
                        <Form.Control
                          type="text"
                          value={entry.description}
                          onChange={e => handleTimeEntryChange(index, 'description', e.target.value)}
                          placeholder="Descrição da atividade"
                        />
                      </Form.Group>
                    </Col>
                  </Row>

                  {timeEntries.length > 1 && (
                    <div className="d-flex justify-content-end mt-2">
                      <Button variant="outline-danger" size="sm" onClick={() => removeTimeEntry(index)}>
                        <Translate contentKey="entity.action.delete">Remover</Translate>
                      </Button>
                    </div>
                  )}
                </div>
              ))}

              <div className="d-flex justify-content-between align-items-center mt-3 mb-2">
                <div>
                  <Button variant="outline-primary" onClick={addTimeEntry}>
                    <Translate contentKey="timesheetHome.addEntry">Adicionar Registro</Translate>
                  </Button>
                </div>
                <div className="text-end">
                  <strong>Total de Horas: </strong>
                  <span className="badge bg-primary fs-6">{totalHours.toFixed(2)}</span>
                </div>
              </div>
            </>
          )}
        </Modal.Body>

        <Modal.Footer>
          <Button variant="secondary" onClick={onClose}>
            <Translate contentKey="entity.action.cancel">Cancelar</Translate>
          </Button>
          <Button variant="primary" type="submit" disabled={updating}>
            {updating ? (
              <>
                <span className="spinner-border spinner-border-sm me-1" role="status" aria-hidden="true"></span>
                <Translate contentKey="entity.action.saving">Salvando...</Translate>
              </>
            ) : (
              <Translate contentKey="entity.action.save">Salvar</Translate>
            )}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
};
