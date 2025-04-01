import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './timesheet-audit.reducer';

export const TimesheetAudit = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const timesheetAuditList = useAppSelector(state => state.timesheetAudit.entities);
  const loading = useAppSelector(state => state.timesheetAudit.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="timesheet-audit-heading" data-cy="TimesheetAuditHeading">
        <Translate contentKey="timesheetSystemApp.timesheetAudit.home.title">Timesheet Audits</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="timesheetSystemApp.timesheetAudit.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/timesheet-audit/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="timesheetSystemApp.timesheetAudit.home.createLabel">Create new Timesheet Audit</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {timesheetAuditList && timesheetAuditList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="timesheetSystemApp.timesheetAudit.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('entityType')}>
                  <Translate contentKey="timesheetSystemApp.timesheetAudit.entityType">Entity Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('entityType')} />
                </th>
                <th className="hand" onClick={sort('entityId')}>
                  <Translate contentKey="timesheetSystemApp.timesheetAudit.entityId">Entity Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('entityId')} />
                </th>
                <th className="hand" onClick={sort('action')}>
                  <Translate contentKey="timesheetSystemApp.timesheetAudit.action">Action</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('action')} />
                </th>
                <th className="hand" onClick={sort('timestamp')}>
                  <Translate contentKey="timesheetSystemApp.timesheetAudit.timestamp">Timestamp</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('timestamp')} />
                </th>
                <th className="hand" onClick={sort('userId')}>
                  <Translate contentKey="timesheetSystemApp.timesheetAudit.userId">User Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('userId')} />
                </th>
                <th className="hand" onClick={sort('oldValues')}>
                  <Translate contentKey="timesheetSystemApp.timesheetAudit.oldValues">Old Values</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('oldValues')} />
                </th>
                <th className="hand" onClick={sort('newValues')}>
                  <Translate contentKey="timesheetSystemApp.timesheetAudit.newValues">New Values</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('newValues')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {timesheetAuditList.map((timesheetAudit, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/timesheet-audit/${timesheetAudit.id}`} color="link" size="sm">
                      {timesheetAudit.id}
                    </Button>
                  </td>
                  <td>{timesheetAudit.entityType}</td>
                  <td>{timesheetAudit.entityId}</td>
                  <td>{timesheetAudit.action}</td>
                  <td>
                    {timesheetAudit.timestamp ? <TextFormat type="date" value={timesheetAudit.timestamp} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{timesheetAudit.userId}</td>
                  <td>{timesheetAudit.oldValues}</td>
                  <td>{timesheetAudit.newValues}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/timesheet-audit/${timesheetAudit.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/timesheet-audit/${timesheetAudit.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/timesheet-audit/${timesheetAudit.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="timesheetSystemApp.timesheetAudit.home.notFound">No Timesheet Audits found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default TimesheetAudit;
