import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITimesheetAudit } from 'app/shared/model/timesheet-audit.model';
import { getEntity, updateEntity, createEntity, reset } from './timesheet-audit.reducer';

export const TimesheetAuditUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const timesheetAuditEntity = useAppSelector(state => state.timesheetAudit.entity);
  const loading = useAppSelector(state => state.timesheetAudit.loading);
  const updating = useAppSelector(state => state.timesheetAudit.updating);
  const updateSuccess = useAppSelector(state => state.timesheetAudit.updateSuccess);

  const handleClose = () => {
    navigate('/timesheet-audit');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.entityId !== undefined && typeof values.entityId !== 'number') {
      values.entityId = Number(values.entityId);
    }
    values.timestamp = convertDateTimeToServer(values.timestamp);
    if (values.userId !== undefined && typeof values.userId !== 'number') {
      values.userId = Number(values.userId);
    }

    const entity = {
      ...timesheetAuditEntity,
      ...values,
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
          timestamp: displayDefaultDateTime(),
        }
      : {
          ...timesheetAuditEntity,
          timestamp: convertDateTimeFromServer(timesheetAuditEntity.timestamp),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="timesheetSystemApp.timesheetAudit.home.createOrEditLabel" data-cy="TimesheetAuditCreateUpdateHeading">
            <Translate contentKey="timesheetSystemApp.timesheetAudit.home.createOrEditLabel">Create or edit a TimesheetAudit</Translate>
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
                  id="timesheet-audit-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetAudit.entityType')}
                id="timesheet-audit-entityType"
                name="entityType"
                data-cy="entityType"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetAudit.entityId')}
                id="timesheet-audit-entityId"
                name="entityId"
                data-cy="entityId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetAudit.action')}
                id="timesheet-audit-action"
                name="action"
                data-cy="action"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetAudit.timestamp')}
                id="timesheet-audit-timestamp"
                name="timestamp"
                data-cy="timestamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetAudit.userId')}
                id="timesheet-audit-userId"
                name="userId"
                data-cy="userId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetAudit.oldValues')}
                id="timesheet-audit-oldValues"
                name="oldValues"
                data-cy="oldValues"
                type="text"
                validate={{
                  maxLength: { value: 10000, message: translate('entity.validation.maxlength', { max: 10000 }) },
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetAudit.newValues')}
                id="timesheet-audit-newValues"
                name="newValues"
                data-cy="newValues"
                type="text"
                validate={{
                  maxLength: { value: 10000, message: translate('entity.validation.maxlength', { max: 10000 }) },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/timesheet-audit" replace color="info">
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

export default TimesheetAuditUpdate;
