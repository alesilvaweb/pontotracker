import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './time-entry.reducer';

export const TimeEntryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const timeEntryEntity = useAppSelector(state => state.timeEntry.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="timeEntryDetailsHeading">
          <Translate contentKey="timesheetSystemApp.timeEntry.detail.title">TimeEntry</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{timeEntryEntity.id}</dd>
          <dt>
            <span id="startTime">
              <Translate contentKey="timesheetSystemApp.timeEntry.startTime">Start Time</Translate>
            </span>
          </dt>
          <dd>
            {timeEntryEntity.startTime ? <TextFormat value={timeEntryEntity.startTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endTime">
              <Translate contentKey="timesheetSystemApp.timeEntry.endTime">End Time</Translate>
            </span>
          </dt>
          <dd>{timeEntryEntity.endTime ? <TextFormat value={timeEntryEntity.endTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="timesheetSystemApp.timeEntry.type">Type</Translate>
            </span>
          </dt>
          <dd>{timeEntryEntity.type}</dd>
          <dt>
            <span id="overtimeCategory">
              <Translate contentKey="timesheetSystemApp.timeEntry.overtimeCategory">Overtime Category</Translate>
            </span>
          </dt>
          <dd>{timeEntryEntity.overtimeCategory}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="timesheetSystemApp.timeEntry.description">Description</Translate>
            </span>
          </dt>
          <dd>{timeEntryEntity.description}</dd>
          <dt>
            <span id="hoursWorked">
              <Translate contentKey="timesheetSystemApp.timeEntry.hoursWorked">Hours Worked</Translate>
            </span>
          </dt>
          <dd>{timeEntryEntity.hoursWorked}</dd>
          <dt>
            <Translate contentKey="timesheetSystemApp.timeEntry.timesheet">Timesheet</Translate>
          </dt>
          <dd>{timeEntryEntity.timesheet ? timeEntryEntity.timesheet.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/time-entry" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/time-entry/${timeEntryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TimeEntryDetail;
