import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITimesheet } from 'app/shared/model/timesheet.model';
import { getEntities as getTimesheets } from 'app/entities/timesheet/timesheet.reducer';
import { ITimeEntry } from 'app/shared/model/time-entry.model';
import { EntryType } from 'app/shared/model/enumerations/entry-type.model';
import { OvertimeCategory } from 'app/shared/model/enumerations/overtime-category.model';
import { getEntity, updateEntity, createEntity, reset } from './time-entry.reducer';

export const TimeEntryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const timesheets = useAppSelector(state => state.timesheet.entities);
  const timeEntryEntity = useAppSelector(state => state.timeEntry.entity);
  const loading = useAppSelector(state => state.timeEntry.loading);
  const updating = useAppSelector(state => state.timeEntry.updating);
  const updateSuccess = useAppSelector(state => state.timeEntry.updateSuccess);
  const entryTypeValues = Object.keys(EntryType);
  const overtimeCategoryValues = Object.keys(OvertimeCategory);

  const handleClose = () => {
    navigate('/time-entry' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTimesheets({}));
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
    values.startTime = convertDateTimeToServer(values.startTime);
    values.endTime = convertDateTimeToServer(values.endTime);
    if (values.hoursWorked !== undefined && typeof values.hoursWorked !== 'number') {
      values.hoursWorked = Number(values.hoursWorked);
    }

    const entity = {
      ...timeEntryEntity,
      ...values,
      timesheet: timesheets.find(it => it.id.toString() === values.timesheet?.toString()),
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
          startTime: displayDefaultDateTime(),
          endTime: displayDefaultDateTime(),
        }
      : {
          type: 'REGULAR',
          overtimeCategory: 'NORMAL',
          ...timeEntryEntity,
          startTime: convertDateTimeFromServer(timeEntryEntity.startTime),
          endTime: convertDateTimeFromServer(timeEntryEntity.endTime),
          timesheet: timeEntryEntity?.timesheet?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="timesheetSystemApp.timeEntry.home.createOrEditLabel" data-cy="TimeEntryCreateUpdateHeading">
            <Translate contentKey="timesheetSystemApp.timeEntry.home.createOrEditLabel">Create or edit a TimeEntry</Translate>
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
                  id="time-entry-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('timesheetSystemApp.timeEntry.startTime')}
                id="time-entry-startTime"
                name="startTime"
                data-cy="startTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timeEntry.endTime')}
                id="time-entry-endTime"
                name="endTime"
                data-cy="endTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timeEntry.type')}
                id="time-entry-type"
                name="type"
                data-cy="type"
                type="select"
              >
                {entryTypeValues.map(entryType => (
                  <option value={entryType} key={entryType}>
                    {translate('timesheetSystemApp.EntryType.' + entryType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('timesheetSystemApp.timeEntry.overtimeCategory')}
                id="time-entry-overtimeCategory"
                name="overtimeCategory"
                data-cy="overtimeCategory"
                type="select"
              >
                {overtimeCategoryValues.map(overtimeCategory => (
                  <option value={overtimeCategory} key={overtimeCategory}>
                    {translate('timesheetSystemApp.OvertimeCategory.' + overtimeCategory)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('timesheetSystemApp.timeEntry.description')}
                id="time-entry-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timeEntry.hoursWorked')}
                id="time-entry-hoursWorked"
                name="hoursWorked"
                data-cy="hoursWorked"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="time-entry-timesheet"
                name="timesheet"
                data-cy="timesheet"
                label={translate('timesheetSystemApp.timeEntry.timesheet')}
                type="select"
                required
              >
                <option value="" key="0" />
                {timesheets
                  ? timesheets.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/time-entry" replace color="info">
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

export default TimeEntryUpdate;
