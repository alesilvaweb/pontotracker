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
import { IUserPreference } from 'app/shared/model/user-preference.model';
import { getEntity, updateEntity, createEntity, reset } from './user-preference.reducer';

export const UserPreferenceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const userPreferenceEntity = useAppSelector(state => state.userPreference.entity);
  const loading = useAppSelector(state => state.userPreference.loading);
  const updating = useAppSelector(state => state.userPreference.updating);
  const updateSuccess = useAppSelector(state => state.userPreference.updateSuccess);

  const handleClose = () => {
    navigate('/user-preference');
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
    if (values.defaultCompanyId !== undefined && typeof values.defaultCompanyId !== 'number') {
      values.defaultCompanyId = Number(values.defaultCompanyId);
    }
    if (values.weekStartDay !== undefined && typeof values.weekStartDay !== 'number') {
      values.weekStartDay = Number(values.weekStartDay);
    }

    const entity = {
      ...userPreferenceEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...userPreferenceEntity,
          user: userPreferenceEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="timesheetSystemApp.userPreference.home.createOrEditLabel" data-cy="UserPreferenceCreateUpdateHeading">
            <Translate contentKey="timesheetSystemApp.userPreference.home.createOrEditLabel">Create or edit a UserPreference</Translate>
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
                  id="user-preference-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('timesheetSystemApp.userPreference.defaultCompanyId')}
                id="user-preference-defaultCompanyId"
                name="defaultCompanyId"
                data-cy="defaultCompanyId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('timesheetSystemApp.userPreference.emailNotifications')}
                id="user-preference-emailNotifications"
                name="emailNotifications"
                data-cy="emailNotifications"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('timesheetSystemApp.userPreference.smsNotifications')}
                id="user-preference-smsNotifications"
                name="smsNotifications"
                data-cy="smsNotifications"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('timesheetSystemApp.userPreference.pushNotifications')}
                id="user-preference-pushNotifications"
                name="pushNotifications"
                data-cy="pushNotifications"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('timesheetSystemApp.userPreference.reportFrequency')}
                id="user-preference-reportFrequency"
                name="reportFrequency"
                data-cy="reportFrequency"
                type="text"
              />
              <ValidatedField
                label={translate('timesheetSystemApp.userPreference.weekStartDay')}
                id="user-preference-weekStartDay"
                name="weekStartDay"
                data-cy="weekStartDay"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  max: { value: 6, message: translate('entity.validation.max', { max: 6 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="user-preference-user"
                name="user"
                data-cy="user"
                label={translate('timesheetSystemApp.userPreference.user')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-preference" replace color="info">
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

export default UserPreferenceUpdate;
