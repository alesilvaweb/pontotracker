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
import { ITimesheetAlert } from 'app/shared/model/timesheet-alert.model';
import { getEntity, updateEntity, createEntity, reset } from './timesheet-alert.reducer';

export const TimesheetAlertUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const timesheetAlertEntity = useAppSelector(state => state.timesheetAlert.entity);
  const loading = useAppSelector(state => state.timesheetAlert.loading);
  const updating = useAppSelector(state => state.timesheetAlert.updating);
  const updateSuccess = useAppSelector(state => state.timesheetAlert.updateSuccess);

  const handleClose = () => {
    navigate('/timesheet-alert' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
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
    if (values.userId !== undefined && typeof values.userId !== 'number') {
      values.userId = Number(values.userId);
    }
    if (values.timesheetId !== undefined && typeof values.timesheetId !== 'number') {
      values.timesheetId = Number(values.timesheetId);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.resolvedAt = convertDateTimeToServer(values.resolvedAt);

    const entity = {
      ...timesheetAlertEntity,
      ...values,
      resolvedBy: users.find(it => it.id.toString() === values.resolvedBy?.toString()),
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
          resolvedAt: displayDefaultDateTime(),
        }
      : {
          ...timesheetAlertEntity,
          createdAt: convertDateTimeFromServer(timesheetAlertEntity.createdAt),
          resolvedAt: convertDateTimeFromServer(timesheetAlertEntity.resolvedAt),
          resolvedBy: timesheetAlertEntity?.resolvedBy?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="timesheetSystemApp.timesheetAlert.home.createOrEditLabel" data-cy="TimesheetAlertCreateUpdateHeading">
            <Translate contentKey="timesheetSystemApp.timesheetAlert.home.createOrEditLabel">Create or edit a TimesheetAlert</Translate>
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
                  id="timesheet-alert-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetAlert.userId')}
                id="timesheet-alert-userId"
                name="userId"
                data-cy="userId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetAlert.timesheetId')}
                id="timesheet-alert-timesheetId"
                name="timesheetId"
                data-cy="timesheetId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetAlert.date')}
                id="timesheet-alert-date"
                name="date"
                data-cy="date"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetAlert.type')}
                id="timesheet-alert-type"
                name="type"
                data-cy="type"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetAlert.message')}
                id="timesheet-alert-message"
                name="message"
                data-cy="message"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetAlert.severity')}
                id="timesheet-alert-severity"
                name="severity"
                data-cy="severity"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetAlert.status')}
                id="timesheet-alert-status"
                name="status"
                data-cy="status"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetAlert.createdAt')}
                id="timesheet-alert-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetAlert.resolvedAt')}
                id="timesheet-alert-resolvedAt"
                name="resolvedAt"
                data-cy="resolvedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetAlert.resolution')}
                id="timesheet-alert-resolution"
                name="resolution"
                data-cy="resolution"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                id="timesheet-alert-resolvedBy"
                name="resolvedBy"
                data-cy="resolvedBy"
                label={translate('timesheetSystemApp.timesheetAlert.resolvedBy')}
                type="select"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/timesheet-alert" replace color="info">
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

export default TimesheetAlertUpdate;
