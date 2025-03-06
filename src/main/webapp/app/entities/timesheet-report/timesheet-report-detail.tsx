import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './timesheet-report.reducer';

export const TimesheetReportDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const timesheetReportEntity = useAppSelector(state => state.timesheetReport.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="timesheetReportDetailsHeading">
          <Translate contentKey="timesheetSystemApp.timesheetReport.detail.title">TimesheetReport</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{timesheetReportEntity.id}</dd>
          <dt>
            <span id="userId">
              <Translate contentKey="timesheetSystemApp.timesheetReport.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{timesheetReportEntity.userId}</dd>
          <dt>
            <span id="userName">
              <Translate contentKey="timesheetSystemApp.timesheetReport.userName">User Name</Translate>
            </span>
          </dt>
          <dd>{timesheetReportEntity.userName}</dd>
          <dt>
            <span id="companyId">
              <Translate contentKey="timesheetSystemApp.timesheetReport.companyId">Company Id</Translate>
            </span>
          </dt>
          <dd>{timesheetReportEntity.companyId}</dd>
          <dt>
            <span id="companyName">
              <Translate contentKey="timesheetSystemApp.timesheetReport.companyName">Company Name</Translate>
            </span>
          </dt>
          <dd>{timesheetReportEntity.companyName}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="timesheetSystemApp.timesheetReport.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {timesheetReportEntity.startDate ? (
              <TextFormat value={timesheetReportEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="timesheetSystemApp.timesheetReport.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            {timesheetReportEntity.endDate ? (
              <TextFormat value={timesheetReportEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="totalRegularHours">
              <Translate contentKey="timesheetSystemApp.timesheetReport.totalRegularHours">Total Regular Hours</Translate>
            </span>
          </dt>
          <dd>{timesheetReportEntity.totalRegularHours}</dd>
          <dt>
            <span id="totalOvertimeHours">
              <Translate contentKey="timesheetSystemApp.timesheetReport.totalOvertimeHours">Total Overtime Hours</Translate>
            </span>
          </dt>
          <dd>{timesheetReportEntity.totalOvertimeHours}</dd>
          <dt>
            <span id="totalAllowance">
              <Translate contentKey="timesheetSystemApp.timesheetReport.totalAllowance">Total Allowance</Translate>
            </span>
          </dt>
          <dd>{timesheetReportEntity.totalAllowance}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="timesheetSystemApp.timesheetReport.status">Status</Translate>
            </span>
          </dt>
          <dd>{timesheetReportEntity.status}</dd>
          <dt>
            <span id="generatedAt">
              <Translate contentKey="timesheetSystemApp.timesheetReport.generatedAt">Generated At</Translate>
            </span>
          </dt>
          <dd>
            {timesheetReportEntity.generatedAt ? (
              <TextFormat value={timesheetReportEntity.generatedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="approvedAt">
              <Translate contentKey="timesheetSystemApp.timesheetReport.approvedAt">Approved At</Translate>
            </span>
          </dt>
          <dd>
            {timesheetReportEntity.approvedAt ? (
              <TextFormat value={timesheetReportEntity.approvedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="comments">
              <Translate contentKey="timesheetSystemApp.timesheetReport.comments">Comments</Translate>
            </span>
          </dt>
          <dd>{timesheetReportEntity.comments}</dd>
          <dt>
            <Translate contentKey="timesheetSystemApp.timesheetReport.generatedBy">Generated By</Translate>
          </dt>
          <dd>{timesheetReportEntity.generatedBy ? timesheetReportEntity.generatedBy.login : ''}</dd>
          <dt>
            <Translate contentKey="timesheetSystemApp.timesheetReport.approvedBy">Approved By</Translate>
          </dt>
          <dd>{timesheetReportEntity.approvedBy ? timesheetReportEntity.approvedBy.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/timesheet-report" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/timesheet-report/${timesheetReportEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TimesheetReportDetail;
