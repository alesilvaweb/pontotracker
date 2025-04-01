import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICompany } from 'app/shared/model/company.model';
import { getEntities as getCompanies } from 'app/entities/company/company.reducer';
import { ICompanyAllowance } from 'app/shared/model/company-allowance.model';
import { getEntity, updateEntity, createEntity, reset } from './company-allowance.reducer';

export const CompanyAllowanceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const companies = useAppSelector(state => state.company.entities);
  const companyAllowanceEntity = useAppSelector(state => state.companyAllowance.entity);
  const loading = useAppSelector(state => state.companyAllowance.loading);
  const updating = useAppSelector(state => state.companyAllowance.updating);
  const updateSuccess = useAppSelector(state => state.companyAllowance.updateSuccess);

  const handleClose = () => {
    navigate('/company-allowance');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

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
    if (values.presentialAllowanceValue !== undefined && typeof values.presentialAllowanceValue !== 'number') {
      values.presentialAllowanceValue = Number(values.presentialAllowanceValue);
    }
    if (values.remoteAllowanceValue !== undefined && typeof values.remoteAllowanceValue !== 'number') {
      values.remoteAllowanceValue = Number(values.remoteAllowanceValue);
    }
    if (values.fullTimeThresholdHours !== undefined && typeof values.fullTimeThresholdHours !== 'number') {
      values.fullTimeThresholdHours = Number(values.fullTimeThresholdHours);
    }
    if (values.partTimeAllowancePercentage !== undefined && typeof values.partTimeAllowancePercentage !== 'number') {
      values.partTimeAllowancePercentage = Number(values.partTimeAllowancePercentage);
    }
    if (values.minimumDistanceToReceive !== undefined && typeof values.minimumDistanceToReceive !== 'number') {
      values.minimumDistanceToReceive = Number(values.minimumDistanceToReceive);
    }
    values.lastUpdated = convertDateTimeToServer(values.lastUpdated);

    const entity = {
      ...companyAllowanceEntity,
      ...values,
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
          lastUpdated: displayDefaultDateTime(),
        }
      : {
          ...companyAllowanceEntity,
          lastUpdated: convertDateTimeFromServer(companyAllowanceEntity.lastUpdated),
          company: companyAllowanceEntity?.company?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="timesheetSystemApp.companyAllowance.home.createOrEditLabel" data-cy="CompanyAllowanceCreateUpdateHeading">
            <Translate contentKey="timesheetSystemApp.companyAllowance.home.createOrEditLabel">Create or edit a CompanyAllowance</Translate>
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
                  id="company-allowance-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('timesheetSystemApp.companyAllowance.presentialAllowanceValue')}
                id="company-allowance-presentialAllowanceValue"
                name="presentialAllowanceValue"
                data-cy="presentialAllowanceValue"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.companyAllowance.remoteAllowanceValue')}
                id="company-allowance-remoteAllowanceValue"
                name="remoteAllowanceValue"
                data-cy="remoteAllowanceValue"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.companyAllowance.fullTimeThresholdHours')}
                id="company-allowance-fullTimeThresholdHours"
                name="fullTimeThresholdHours"
                data-cy="fullTimeThresholdHours"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.companyAllowance.partTimeAllowancePercentage')}
                id="company-allowance-partTimeAllowancePercentage"
                name="partTimeAllowancePercentage"
                data-cy="partTimeAllowancePercentage"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  max: { value: 100, message: translate('entity.validation.max', { max: 100 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.companyAllowance.considerWorkDistance')}
                id="company-allowance-considerWorkDistance"
                name="considerWorkDistance"
                data-cy="considerWorkDistance"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('timesheetSystemApp.companyAllowance.minimumDistanceToReceive')}
                id="company-allowance-minimumDistanceToReceive"
                name="minimumDistanceToReceive"
                data-cy="minimumDistanceToReceive"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.companyAllowance.active')}
                id="company-allowance-active"
                name="active"
                data-cy="active"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('timesheetSystemApp.companyAllowance.lastUpdated')}
                id="company-allowance-lastUpdated"
                name="lastUpdated"
                data-cy="lastUpdated"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="company-allowance-company"
                name="company"
                data-cy="company"
                label={translate('timesheetSystemApp.companyAllowance.company')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/company-allowance" replace color="info">
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

export default CompanyAllowanceUpdate;
