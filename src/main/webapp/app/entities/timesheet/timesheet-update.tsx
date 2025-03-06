import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { ICompany } from 'app/shared/model/company.model';
import { getEntities as getCompanies } from 'app/entities/company/company.reducer';
import { ITimesheet } from 'app/shared/model/timesheet.model';
import { WorkModality } from 'app/shared/model/enumerations/work-modality.model';
import { getEntity, updateEntity, createEntity, reset } from './timesheet.reducer';

export const TimesheetUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const companies = useAppSelector(state => state.company.entities);
  const timesheetEntity = useAppSelector(state => state.timesheet.entity);
  const loading = useAppSelector(state => state.timesheet.loading);
  const updating = useAppSelector(state => state.timesheet.updating);
  const updateSuccess = useAppSelector(state => state.timesheet.updateSuccess);
  const workModalityValues = Object.keys(WorkModality);

  const handleClose = () => {
    navigate('/timesheet' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getCompanies({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.totalHours !== undefined && typeof values.totalHours !== 'number') {
      values.totalHours = Number(values.totalHours);
    }
    if (values.overtimeHours !== undefined && typeof values.overtimeHours !== 'number') {
      values.overtimeHours = Number(values.overtimeHours);
    }
    if (values.allowanceValue !== undefined && typeof values.allowanceValue !== 'number') {
      values.allowanceValue = Number(values.allowanceValue);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);
    values.approvedAt = convertDateTimeToServer(values.approvedAt);

    const entity = {
      ...timesheetEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
      company: companies.find(it => it.id.toString() === values.company?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
          approvedAt: displayDefaultDateTime(),
        }
      : {
          modality: 'REMOTE',
          ...timesheetEntity,
          createdAt: convertDateTimeFromServer(timesheetEntity.createdAt),
          updatedAt: convertDateTimeFromServer(timesheetEntity.updatedAt),
          approvedAt: convertDateTimeFromServer(timesheetEntity.approvedAt),
          user: timesheetEntity?.user?.id,
          company: timesheetEntity?.company?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="timesheetSystemApp.timesheet.home.createOrEditLabel" data-cy="TimesheetCreateUpdateHeading">
            <Translate contentKey="timesheetSystemApp.timesheet.home.createOrEditLabel">Create or edit a Timesheet</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="timesheet-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('timesheetSystemApp.timesheet.date')}
                id="timesheet-date"
                name="date"
                data-cy="date"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheet.modality')}
                id="timesheet-modality"
                name="modality"
                data-cy="modality"
                type="select"
              >
                {workModalityValues.map(workModality => (
                  <option value={workModality} key={workModality}>
                    {translate('timesheetSystemApp.WorkModality.' + workModality)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('timesheetSystemApp.timesheet.classification')}
                id="timesheet-classification"
                name="classification"
                data-cy="classification"
                type="text"
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheet.description')}
                id="timesheet-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 2000, message: translate('entity.validation.maxlength', { max: 2000 }) },
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheet.totalHours')}
                id="timesheet-totalHours"
                name="totalHours"
                data-cy="totalHours"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheet.overtimeHours')}
                id="timesheet-overtimeHours"
                name="overtimeHours"
                data-cy="overtimeHours"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheet.allowanceValue')}
                id="timesheet-allowanceValue"
                name="allowanceValue"
                data-cy="allowanceValue"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheet.status')}
                id="timesheet-status"
                name="status"
                data-cy="status"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheet.createdAt')}
                id="timesheet-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheet.updatedAt')}
                id="timesheet-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheet.approvedAt')}
                id="timesheet-approvedAt"
                name="approvedAt"
                data-cy="approvedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheet.approvedBy')}
                id="timesheet-approvedBy"
                name="approvedBy"
                data-cy="approvedBy"
                type="text"
              />
              <ValidatedField
                id="timesheet-user"
                name="user"
                data-cy="user"
                label={translate('timesheetSystemApp.timesheet.user')}
                type="select"
                required
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="timesheet-company"
                name="company"
                data-cy="company"
                label={translate('timesheetSystemApp.timesheet.company')}
                type="select"
                required
              >
                <option value="" key="0" />
                {companies
                  ? companies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/timesheet" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default TimesheetUpdate;
