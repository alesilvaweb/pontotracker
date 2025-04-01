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

import { getEntities } from './system-config.reducer';

export const SystemConfig = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const systemConfigList = useAppSelector(state => state.systemConfig.entities);
  const loading = useAppSelector(state => state.systemConfig.loading);

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
      <h2 id="system-config-heading" data-cy="SystemConfigHeading">
        <Translate contentKey="timesheetSystemApp.systemConfig.home.title">System Configs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="timesheetSystemApp.systemConfig.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/system-config/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="timesheetSystemApp.systemConfig.home.createLabel">Create new System Config</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {systemConfigList && systemConfigList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="timesheetSystemApp.systemConfig.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('dailyWorkHours')}>
                  <Translate contentKey="timesheetSystemApp.systemConfig.dailyWorkHours">Daily Work Hours</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dailyWorkHours')} />
                </th>
                <th className="hand" onClick={sort('weeklyWorkHours')}>
                  <Translate contentKey="timesheetSystemApp.systemConfig.weeklyWorkHours">Weekly Work Hours</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('weeklyWorkHours')} />
                </th>
                <th className="hand" onClick={sort('overtimeNormalRate')}>
                  <Translate contentKey="timesheetSystemApp.systemConfig.overtimeNormalRate">Overtime Normal Rate</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('overtimeNormalRate')} />
                </th>
                <th className="hand" onClick={sort('overtimeSpecialRate')}>
                  <Translate contentKey="timesheetSystemApp.systemConfig.overtimeSpecialRate">Overtime Special Rate</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('overtimeSpecialRate')} />
                </th>
                <th className="hand" onClick={sort('lastUpdated')}>
                  <Translate contentKey="timesheetSystemApp.systemConfig.lastUpdated">Last Updated</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastUpdated')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {systemConfigList.map((systemConfig, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/system-config/${systemConfig.id}`} color="link" size="sm">
                      {systemConfig.id}
                    </Button>
                  </td>
                  <td>{systemConfig.dailyWorkHours}</td>
                  <td>{systemConfig.weeklyWorkHours}</td>
                  <td>{systemConfig.overtimeNormalRate}</td>
                  <td>{systemConfig.overtimeSpecialRate}</td>
                  <td>
                    {systemConfig.lastUpdated ? <TextFormat type="date" value={systemConfig.lastUpdated} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/system-config/${systemConfig.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/system-config/${systemConfig.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/system-config/${systemConfig.id}/delete`)}
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
              <Translate contentKey="timesheetSystemApp.systemConfig.home.notFound">No System Configs found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default SystemConfig;
