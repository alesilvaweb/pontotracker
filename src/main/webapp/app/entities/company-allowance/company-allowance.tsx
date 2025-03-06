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

import { getEntities } from './company-allowance.reducer';

export const CompanyAllowance = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const companyAllowanceList = useAppSelector(state => state.companyAllowance.entities);
  const loading = useAppSelector(state => state.companyAllowance.loading);

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
      <h2 id="company-allowance-heading" data-cy="CompanyAllowanceHeading">
        <Translate contentKey="timesheetSystemApp.companyAllowance.home.title">Company Allowances</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="timesheetSystemApp.companyAllowance.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/company-allowance/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="timesheetSystemApp.companyAllowance.home.createLabel">Create new Company Allowance</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {companyAllowanceList && companyAllowanceList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="timesheetSystemApp.companyAllowance.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('presentialAllowanceValue')}>
                  <Translate contentKey="timesheetSystemApp.companyAllowance.presentialAllowanceValue">
                    Presential Allowance Value
                  </Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('presentialAllowanceValue')} />
                </th>
                <th className="hand" onClick={sort('remoteAllowanceValue')}>
                  <Translate contentKey="timesheetSystemApp.companyAllowance.remoteAllowanceValue">Remote Allowance Value</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('remoteAllowanceValue')} />
                </th>
                <th className="hand" onClick={sort('fullTimeThresholdHours')}>
                  <Translate contentKey="timesheetSystemApp.companyAllowance.fullTimeThresholdHours">Full Time Threshold Hours</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fullTimeThresholdHours')} />
                </th>
                <th className="hand" onClick={sort('partTimeAllowancePercentage')}>
                  <Translate contentKey="timesheetSystemApp.companyAllowance.partTimeAllowancePercentage">
                    Part Time Allowance Percentage
                  </Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('partTimeAllowancePercentage')} />
                </th>
                <th className="hand" onClick={sort('considerWorkDistance')}>
                  <Translate contentKey="timesheetSystemApp.companyAllowance.considerWorkDistance">Consider Work Distance</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('considerWorkDistance')} />
                </th>
                <th className="hand" onClick={sort('minimumDistanceToReceive')}>
                  <Translate contentKey="timesheetSystemApp.companyAllowance.minimumDistanceToReceive">
                    Minimum Distance To Receive
                  </Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('minimumDistanceToReceive')} />
                </th>
                <th className="hand" onClick={sort('active')}>
                  <Translate contentKey="timesheetSystemApp.companyAllowance.active">Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('active')} />
                </th>
                <th className="hand" onClick={sort('lastUpdated')}>
                  <Translate contentKey="timesheetSystemApp.companyAllowance.lastUpdated">Last Updated</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastUpdated')} />
                </th>
                <th>
                  <Translate contentKey="timesheetSystemApp.companyAllowance.company">Company</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {companyAllowanceList.map((companyAllowance, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/company-allowance/${companyAllowance.id}`} color="link" size="sm">
                      {companyAllowance.id}
                    </Button>
                  </td>
                  <td>{companyAllowance.presentialAllowanceValue}</td>
                  <td>{companyAllowance.remoteAllowanceValue}</td>
                  <td>{companyAllowance.fullTimeThresholdHours}</td>
                  <td>{companyAllowance.partTimeAllowancePercentage}</td>
                  <td>{companyAllowance.considerWorkDistance ? 'true' : 'false'}</td>
                  <td>{companyAllowance.minimumDistanceToReceive}</td>
                  <td>{companyAllowance.active ? 'true' : 'false'}</td>
                  <td>
                    {companyAllowance.lastUpdated ? (
                      <TextFormat type="date" value={companyAllowance.lastUpdated} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {companyAllowance.company ? (
                      <Link to={`/company/${companyAllowance.company.id}`}>{companyAllowance.company.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/company-allowance/${companyAllowance.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/company-allowance/${companyAllowance.id}/edit`}
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
                        onClick={() => (window.location.href = `/company-allowance/${companyAllowance.id}/delete`)}
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
              <Translate contentKey="timesheetSystemApp.companyAllowance.home.notFound">No Company Allowances found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default CompanyAllowance;
