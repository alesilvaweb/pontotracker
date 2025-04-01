import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './timesheet-alert.reducer';

export const TimesheetAlertDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const timesheetAlertEntity = useAppSelector(state => state.timesheetAlert.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="timesheetAlertDetailsHeading">
          <Translate contentKey="timesheetSystemApp.timesheetAlert.detail.title">TimesheetAlert</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{timesheetAlertEntity.id}</dd>
          <dt>
            <span id="userId">
              <Translate contentKey="timesheetSystemApp.timesheetAlert.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{timesheetAlertEntity.userId}</dd>
          <dt>
            <span id="timesheetId">
              <Translate contentKey="timesheetSystemApp.timesheetAlert.timesheetId">Timesheet Id</Translate>
            </span>
          </dt>
          <dd>{timesheetAlertEntity.timesheetId}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="timesheetSystemApp.timesheetAlert.date">Date</Translate>
            </span>
          </dt>
          <dd>
            {timesheetAlertEntity.date ? <TextFormat value={timesheetAlertEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="type">
              <Translate contentKey="timesheetSystemApp.timesheetAlert.type">Type</Translate>
            </span>
          </dt>
          <dd>{timesheetAlertEntity.type}</dd>
          <dt>
            <span id="message">
              <Translate contentKey="timesheetSystemApp.timesheetAlert.message">Message</Translate>
            </span>
          </dt>
          <dd>{timesheetAlertEntity.message}</dd>
          <dt>
            <span id="severity">
              <Translate contentKey="timesheetSystemApp.timesheetAlert.severity">Severity</Translate>
            </span>
          </dt>
          <dd>{timesheetAlertEntity.severity}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="timesheetSystemApp.timesheetAlert.status">Status</Translate>
            </span>
          </dt>
          <dd>{timesheetAlertEntity.status}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="timesheetSystemApp.timesheetAlert.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {timesheetAlertEntity.createdAt ? (
              <TextFormat value={timesheetAlertEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="resolvedAt">
              <Translate contentKey="timesheetSystemApp.timesheetAlert.resolvedAt">Resolved At</Translate>
            </span>
          </dt>
          <dd>
            {timesheetAlertEntity.resolvedAt ? (
              <TextFormat value={timesheetAlertEntity.resolvedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="resolution">
              <Translate contentKey="timesheetSystemApp.timesheetAlert.resolution">Resolution</Translate>
            </span>
          </dt>
          <dd>{timesheetAlertEntity.resolution}</dd>
          <dt>
            <Translate contentKey="timesheetSystemApp.timesheetAlert.resolvedBy">Resolved By</Translate>
          </dt>
          <dd>{timesheetAlertEntity.resolvedBy ? timesheetAlertEntity.resolvedBy.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/timesheet-alert" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/timesheet-alert/${timesheetAlertEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TimesheetAlertDetail;
