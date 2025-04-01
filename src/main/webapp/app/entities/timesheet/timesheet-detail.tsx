import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './timesheet.reducer';

export const TimesheetDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const timesheetEntity = useAppSelector(state => state.timesheet.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="timesheetDetailsHeading">
          <Translate contentKey="timesheetSystemApp.timesheet.detail.title">Timesheet</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{timesheetEntity.id}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="timesheetSystemApp.timesheet.date">Date</Translate>
            </span>
          </dt>
          <dd>{timesheetEntity.date ? <TextFormat value={timesheetEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="modality">
              <Translate contentKey="timesheetSystemApp.timesheet.modality">Modality</Translate>
            </span>
          </dt>
          <dd>{timesheetEntity.modality}</dd>
          <dt>
            <span id="classification">
              <Translate contentKey="timesheetSystemApp.timesheet.classification">Classification</Translate>
            </span>
          </dt>
          <dd>{timesheetEntity.classification}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="timesheetSystemApp.timesheet.description">Description</Translate>
            </span>
          </dt>
          <dd>{timesheetEntity.description}</dd>
          <dt>
            <span id="totalHours">
              <Translate contentKey="timesheetSystemApp.timesheet.totalHours">Total Hours</Translate>
            </span>
          </dt>
          <dd>{timesheetEntity.totalHours}</dd>
          <dt>
            <span id="overtimeHours">
              <Translate contentKey="timesheetSystemApp.timesheet.overtimeHours">Overtime Hours</Translate>
            </span>
          </dt>
          <dd>{timesheetEntity.overtimeHours}</dd>
          <dt>
            <span id="allowanceValue">
              <Translate contentKey="timesheetSystemApp.timesheet.allowanceValue">Allowance Value</Translate>
            </span>
          </dt>
          <dd>{timesheetEntity.allowanceValue}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="timesheetSystemApp.timesheet.status">Status</Translate>
            </span>
          </dt>
          <dd>{timesheetEntity.status}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="timesheetSystemApp.timesheet.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {timesheetEntity.createdAt ? <TextFormat value={timesheetEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="timesheetSystemApp.timesheet.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {timesheetEntity.updatedAt ? <TextFormat value={timesheetEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="approvedAt">
              <Translate contentKey="timesheetSystemApp.timesheet.approvedAt">Approved At</Translate>
            </span>
          </dt>
          <dd>
            {timesheetEntity.approvedAt ? <TextFormat value={timesheetEntity.approvedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="approvedBy">
              <Translate contentKey="timesheetSystemApp.timesheet.approvedBy">Approved By</Translate>
            </span>
          </dt>
          <dd>{timesheetEntity.approvedBy}</dd>
          <dt>
            <Translate contentKey="timesheetSystemApp.timesheet.user">User</Translate>
          </dt>
          <dd>{timesheetEntity.user ? timesheetEntity.user.login : ''}</dd>
          <dt>
            <Translate contentKey="timesheetSystemApp.timesheet.company">Company</Translate>
          </dt>
          <dd>{timesheetEntity.company ? timesheetEntity.company.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/timesheet" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/timesheet/${timesheetEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TimesheetDetail;
