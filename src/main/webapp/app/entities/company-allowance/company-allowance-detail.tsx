import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './company-allowance.reducer';

export const CompanyAllowanceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const companyAllowanceEntity = useAppSelector(state => state.companyAllowance.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="companyAllowanceDetailsHeading">
          <Translate contentKey="timesheetSystemApp.companyAllowance.detail.title">CompanyAllowance</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{companyAllowanceEntity.id}</dd>
          <dt>
            <span id="presentialAllowanceValue">
              <Translate contentKey="timesheetSystemApp.companyAllowance.presentialAllowanceValue">Presential Allowance Value</Translate>
            </span>
          </dt>
          <dd>{companyAllowanceEntity.presentialAllowanceValue}</dd>
          <dt>
            <span id="remoteAllowanceValue">
              <Translate contentKey="timesheetSystemApp.companyAllowance.remoteAllowanceValue">Remote Allowance Value</Translate>
            </span>
          </dt>
          <dd>{companyAllowanceEntity.remoteAllowanceValue}</dd>
          <dt>
            <span id="fullTimeThresholdHours">
              <Translate contentKey="timesheetSystemApp.companyAllowance.fullTimeThresholdHours">Full Time Threshold Hours</Translate>
            </span>
          </dt>
          <dd>{companyAllowanceEntity.fullTimeThresholdHours}</dd>
          <dt>
            <span id="partTimeAllowancePercentage">
              <Translate contentKey="timesheetSystemApp.companyAllowance.partTimeAllowancePercentage">
                Part Time Allowance Percentage
              </Translate>
            </span>
          </dt>
          <dd>{companyAllowanceEntity.partTimeAllowancePercentage}</dd>
          <dt>
            <span id="considerWorkDistance">
              <Translate contentKey="timesheetSystemApp.companyAllowance.considerWorkDistance">Consider Work Distance</Translate>
            </span>
          </dt>
          <dd>{companyAllowanceEntity.considerWorkDistance ? 'true' : 'false'}</dd>
          <dt>
            <span id="minimumDistanceToReceive">
              <Translate contentKey="timesheetSystemApp.companyAllowance.minimumDistanceToReceive">Minimum Distance To Receive</Translate>
            </span>
          </dt>
          <dd>{companyAllowanceEntity.minimumDistanceToReceive}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="timesheetSystemApp.companyAllowance.active">Active</Translate>
            </span>
          </dt>
          <dd>{companyAllowanceEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <span id="lastUpdated">
              <Translate contentKey="timesheetSystemApp.companyAllowance.lastUpdated">Last Updated</Translate>
            </span>
          </dt>
          <dd>
            {companyAllowanceEntity.lastUpdated ? (
              <TextFormat value={companyAllowanceEntity.lastUpdated} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="timesheetSystemApp.companyAllowance.company">Company</Translate>
          </dt>
          <dd>{companyAllowanceEntity.company ? companyAllowanceEntity.company.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/company-allowance" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/company-allowance/${companyAllowanceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CompanyAllowanceDetail;
