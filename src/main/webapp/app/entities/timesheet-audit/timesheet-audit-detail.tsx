import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './timesheet-audit.reducer';

export const TimesheetAuditDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const timesheetAuditEntity = useAppSelector(state => state.timesheetAudit.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="timesheetAuditDetailsHeading">
          <Translate contentKey="timesheetSystemApp.timesheetAudit.detail.title">TimesheetAudit</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{timesheetAuditEntity.id}</dd>
          <dt>
            <span id="entityType">
              <Translate contentKey="timesheetSystemApp.timesheetAudit.entityType">Entity Type</Translate>
            </span>
          </dt>
          <dd>{timesheetAuditEntity.entityType}</dd>
          <dt>
            <span id="entityId">
              <Translate contentKey="timesheetSystemApp.timesheetAudit.entityId">Entity Id</Translate>
            </span>
          </dt>
          <dd>{timesheetAuditEntity.entityId}</dd>
          <dt>
            <span id="action">
              <Translate contentKey="timesheetSystemApp.timesheetAudit.action">Action</Translate>
            </span>
          </dt>
          <dd>{timesheetAuditEntity.action}</dd>
          <dt>
            <span id="timestamp">
              <Translate contentKey="timesheetSystemApp.timesheetAudit.timestamp">Timestamp</Translate>
            </span>
          </dt>
          <dd>
            {timesheetAuditEntity.timestamp ? (
              <TextFormat value={timesheetAuditEntity.timestamp} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="userId">
              <Translate contentKey="timesheetSystemApp.timesheetAudit.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{timesheetAuditEntity.userId}</dd>
          <dt>
            <span id="oldValues">
              <Translate contentKey="timesheetSystemApp.timesheetAudit.oldValues">Old Values</Translate>
            </span>
          </dt>
          <dd>{timesheetAuditEntity.oldValues}</dd>
          <dt>
            <span id="newValues">
              <Translate contentKey="timesheetSystemApp.timesheetAudit.newValues">New Values</Translate>
            </span>
          </dt>
          <dd>{timesheetAuditEntity.newValues}</dd>
        </dl>
        <Button tag={Link} to="/timesheet-audit" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/timesheet-audit/${timesheetAuditEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TimesheetAuditDetail;
