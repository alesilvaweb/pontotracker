import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-preference.reducer';

export const UserPreferenceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userPreferenceEntity = useAppSelector(state => state.userPreference.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userPreferenceDetailsHeading">
          <Translate contentKey="timesheetSystemApp.userPreference.detail.title">UserPreference</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userPreferenceEntity.id}</dd>
          <dt>
            <span id="defaultCompanyId">
              <Translate contentKey="timesheetSystemApp.userPreference.defaultCompanyId">Default Company Id</Translate>
            </span>
          </dt>
          <dd>{userPreferenceEntity.defaultCompanyId}</dd>
          <dt>
            <span id="emailNotifications">
              <Translate contentKey="timesheetSystemApp.userPreference.emailNotifications">Email Notifications</Translate>
            </span>
          </dt>
          <dd>{userPreferenceEntity.emailNotifications ? 'true' : 'false'}</dd>
          <dt>
            <span id="smsNotifications">
              <Translate contentKey="timesheetSystemApp.userPreference.smsNotifications">Sms Notifications</Translate>
            </span>
          </dt>
          <dd>{userPreferenceEntity.smsNotifications ? 'true' : 'false'}</dd>
          <dt>
            <span id="pushNotifications">
              <Translate contentKey="timesheetSystemApp.userPreference.pushNotifications">Push Notifications</Translate>
            </span>
          </dt>
          <dd>{userPreferenceEntity.pushNotifications ? 'true' : 'false'}</dd>
          <dt>
            <span id="reportFrequency">
              <Translate contentKey="timesheetSystemApp.userPreference.reportFrequency">Report Frequency</Translate>
            </span>
          </dt>
          <dd>{userPreferenceEntity.reportFrequency}</dd>
          <dt>
            <span id="weekStartDay">
              <Translate contentKey="timesheetSystemApp.userPreference.weekStartDay">Week Start Day</Translate>
            </span>
          </dt>
          <dd>{userPreferenceEntity.weekStartDay}</dd>
          <dt>
            <Translate contentKey="timesheetSystemApp.userPreference.user">User</Translate>
          </dt>
          <dd>{userPreferenceEntity.user ? userPreferenceEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-preference" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-preference/${userPreferenceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserPreferenceDetail;
