import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISystemConfig } from 'app/shared/model/system-config.model';
import { getEntity, updateEntity, createEntity, reset } from './system-config.reducer';

export const SystemConfigUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const systemConfigEntity = useAppSelector(state => state.systemConfig.entity);
  const loading = useAppSelector(state => state.systemConfig.loading);
  const updating = useAppSelector(state => state.systemConfig.updating);
  const updateSuccess = useAppSelector(state => state.systemConfig.updateSuccess);

  const handleClose = () => {
    navigate('/system-config');
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
    if (values.dailyWorkHours !== undefined && typeof values.dailyWorkHours !== 'number') {
      values.dailyWorkHours = Number(values.dailyWorkHours);
    }
    if (values.weeklyWorkHours !== undefined && typeof values.weeklyWorkHours !== 'number') {
      values.weeklyWorkHours = Number(values.weeklyWorkHours);
    }
    if (values.overtimeNormalRate !== undefined && typeof values.overtimeNormalRate !== 'number') {
      values.overtimeNormalRate = Number(values.overtimeNormalRate);
    }
    if (values.overtimeSpecialRate !== undefined && typeof values.overtimeSpecialRate !== 'number') {
      values.overtimeSpecialRate = Number(values.overtimeSpecialRate);
    }
    values.lastUpdated = convertDateTimeToServer(values.lastUpdated);

    const entity = {
      ...systemConfigEntity,
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
          lastUpdated: displayDefaultDateTime(),
        }
      : {
          ...systemConfigEntity,
          lastUpdated: convertDateTimeFromServer(systemConfigEntity.lastUpdated),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="timesheetSystemApp.systemConfig.home.createOrEditLabel" data-cy="SystemConfigCreateUpdateHeading">
            <Translate contentKey="timesheetSystemApp.systemConfig.home.createOrEditLabel">Create or edit a SystemConfig</Translate>
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
                  id="system-config-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('timesheetSystemApp.systemConfig.dailyWorkHours')}
                id="system-config-dailyWorkHours"
                name="dailyWorkHours"
                data-cy="dailyWorkHours"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.systemConfig.weeklyWorkHours')}
                id="system-config-weeklyWorkHours"
                name="weeklyWorkHours"
                data-cy="weeklyWorkHours"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.systemConfig.overtimeNormalRate')}
                id="system-config-overtimeNormalRate"
                name="overtimeNormalRate"
                data-cy="overtimeNormalRate"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.systemConfig.overtimeSpecialRate')}
                id="system-config-overtimeSpecialRate"
                name="overtimeSpecialRate"
                data-cy="overtimeSpecialRate"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.systemConfig.lastUpdated')}
                id="system-config-lastUpdated"
                name="lastUpdated"
                data-cy="lastUpdated"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/system-config" replace color="info">
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

export default SystemConfigUpdate;
