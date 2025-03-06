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
import { ITimesheetReport } from 'app/shared/model/timesheet-report.model';
import { getEntity, updateEntity, createEntity, reset } from './timesheet-report.reducer';

export const TimesheetReportUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const timesheetReportEntity = useAppSelector(state => state.timesheetReport.entity);
  const loading = useAppSelector(state => state.timesheetReport.loading);
  const updating = useAppSelector(state => state.timesheetReport.updating);
  const updateSuccess = useAppSelector(state => state.timesheetReport.updateSuccess);

  const handleClose = () => {
    navigate('/timesheet-report' + location.search);
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
    if (values.companyId !== undefined && typeof values.companyId !== 'number') {
      values.companyId = Number(values.companyId);
    }
    if (values.totalRegularHours !== undefined && typeof values.totalRegularHours !== 'number') {
      values.totalRegularHours = Number(values.totalRegularHours);
    }
    if (values.totalOvertimeHours !== undefined && typeof values.totalOvertimeHours !== 'number') {
      values.totalOvertimeHours = Number(values.totalOvertimeHours);
    }
    if (values.totalAllowance !== undefined && typeof values.totalAllowance !== 'number') {
      values.totalAllowance = Number(values.totalAllowance);
    }
    values.generatedAt = convertDateTimeToServer(values.generatedAt);
    values.approvedAt = convertDateTimeToServer(values.approvedAt);

    const entity = {
      ...timesheetReportEntity,
      ...values,
      generatedBy: users.find(it => it.id.toString() === values.generatedBy?.toString()),
      approvedBy: users.find(it => it.id.toString() === values.approvedBy?.toString()),
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
          generatedAt: displayDefaultDateTime(),
          approvedAt: displayDefaultDateTime(),
        }
      : {
          ...timesheetReportEntity,
          generatedAt: convertDateTimeFromServer(timesheetReportEntity.generatedAt),
          approvedAt: convertDateTimeFromServer(timesheetReportEntity.approvedAt),
          generatedBy: timesheetReportEntity?.generatedBy?.id,
          approvedBy: timesheetReportEntity?.approvedBy?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="timesheetSystemApp.timesheetReport.home.createOrEditLabel" data-cy="TimesheetReportCreateUpdateHeading">
            <Translate contentKey="timesheetSystemApp.timesheetReport.home.createOrEditLabel">Create or edit a TimesheetReport</Translate>
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
                  id="timesheet-report-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetReport.userId')}
                id="timesheet-report-userId"
                name="userId"
                data-cy="userId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetReport.userName')}
                id="timesheet-report-userName"
                name="userName"
                data-cy="userName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetReport.companyId')}
                id="timesheet-report-companyId"
                name="companyId"
                data-cy="companyId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetReport.companyName')}
                id="timesheet-report-companyName"
                name="companyName"
                data-cy="companyName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetReport.startDate')}
                id="timesheet-report-startDate"
                name="startDate"
                data-cy="startDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetReport.endDate')}
                id="timesheet-report-endDate"
                name="endDate"
                data-cy="endDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetReport.totalRegularHours')}
                id="timesheet-report-totalRegularHours"
                name="totalRegularHours"
                data-cy="totalRegularHours"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetReport.totalOvertimeHours')}
                id="timesheet-report-totalOvertimeHours"
                name="totalOvertimeHours"
                data-cy="totalOvertimeHours"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetReport.totalAllowance')}
                id="timesheet-report-totalAllowance"
                name="totalAllowance"
                data-cy="totalAllowance"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetReport.status')}
                id="timesheet-report-status"
                name="status"
                data-cy="status"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetReport.generatedAt')}
                id="timesheet-report-generatedAt"
                name="generatedAt"
                data-cy="generatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetReport.approvedAt')}
                id="timesheet-report-approvedAt"
                name="approvedAt"
                data-cy="approvedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('timesheetSystemApp.timesheetReport.comments')}
                id="timesheet-report-comments"
                name="comments"
                data-cy="comments"
                type="text"
                validate={{
                  maxLength: { value: 1000, message: translate('entity.validation.maxlength', { max: 1000 }) },
                }}
              />
              <ValidatedField
                id="timesheet-report-generatedBy"
                name="generatedBy"
                data-cy="generatedBy"
                label={translate('timesheetSystemApp.timesheetReport.generatedBy')}
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
                id="timesheet-report-approvedBy"
                name="approvedBy"
                data-cy="approvedBy"
                label={translate('timesheetSystemApp.timesheetReport.approvedBy')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/timesheet-report" replace color="info">
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

export default TimesheetReportUpdate;
