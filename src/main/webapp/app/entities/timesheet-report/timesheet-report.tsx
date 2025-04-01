import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './timesheet-report.reducer';

export const TimesheetReport = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const timesheetReportList = useAppSelector(state => state.timesheetReport.entities);
  const loading = useAppSelector(state => state.timesheetReport.loading);
  const totalItems = useAppSelector(state => state.timesheetReport.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="timesheet-report-heading" data-cy="TimesheetReportHeading">
        <Translate contentKey="timesheetSystemApp.timesheetReport.home.title">Timesheet Reports</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="timesheetSystemApp.timesheetReport.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/timesheet-report/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="timesheetSystemApp.timesheetReport.home.createLabel">Create new Timesheet Report</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {timesheetReportList && timesheetReportList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="timesheetSystemApp.timesheetReport.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('userId')}>
                  <Translate contentKey="timesheetSystemApp.timesheetReport.userId">User Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('userId')} />
                </th>
                <th className="hand" onClick={sort('userName')}>
                  <Translate contentKey="timesheetSystemApp.timesheetReport.userName">User Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('userName')} />
                </th>
                <th className="hand" onClick={sort('companyId')}>
                  <Translate contentKey="timesheetSystemApp.timesheetReport.companyId">Company Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('companyId')} />
                </th>
                <th className="hand" onClick={sort('companyName')}>
                  <Translate contentKey="timesheetSystemApp.timesheetReport.companyName">Company Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('companyName')} />
                </th>
                <th className="hand" onClick={sort('startDate')}>
                  <Translate contentKey="timesheetSystemApp.timesheetReport.startDate">Start Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startDate')} />
                </th>
                <th className="hand" onClick={sort('endDate')}>
                  <Translate contentKey="timesheetSystemApp.timesheetReport.endDate">End Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endDate')} />
                </th>
                <th className="hand" onClick={sort('totalRegularHours')}>
                  <Translate contentKey="timesheetSystemApp.timesheetReport.totalRegularHours">Total Regular Hours</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalRegularHours')} />
                </th>
                <th className="hand" onClick={sort('totalOvertimeHours')}>
                  <Translate contentKey="timesheetSystemApp.timesheetReport.totalOvertimeHours">Total Overtime Hours</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalOvertimeHours')} />
                </th>
                <th className="hand" onClick={sort('totalAllowance')}>
                  <Translate contentKey="timesheetSystemApp.timesheetReport.totalAllowance">Total Allowance</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalAllowance')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="timesheetSystemApp.timesheetReport.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('generatedAt')}>
                  <Translate contentKey="timesheetSystemApp.timesheetReport.generatedAt">Generated At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('generatedAt')} />
                </th>
                <th className="hand" onClick={sort('approvedAt')}>
                  <Translate contentKey="timesheetSystemApp.timesheetReport.approvedAt">Approved At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('approvedAt')} />
                </th>
                <th className="hand" onClick={sort('comments')}>
                  <Translate contentKey="timesheetSystemApp.timesheetReport.comments">Comments</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('comments')} />
                </th>
                <th>
                  <Translate contentKey="timesheetSystemApp.timesheetReport.generatedBy">Generated By</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="timesheetSystemApp.timesheetReport.approvedBy">Approved By</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {timesheetReportList.map((timesheetReport, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/timesheet-report/${timesheetReport.id}`} color="link" size="sm">
                      {timesheetReport.id}
                    </Button>
                  </td>
                  <td>{timesheetReport.userId}</td>
                  <td>{timesheetReport.userName}</td>
                  <td>{timesheetReport.companyId}</td>
                  <td>{timesheetReport.companyName}</td>
                  <td>
                    {timesheetReport.startDate ? (
                      <TextFormat type="date" value={timesheetReport.startDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {timesheetReport.endDate ? (
                      <TextFormat type="date" value={timesheetReport.endDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{timesheetReport.totalRegularHours}</td>
                  <td>{timesheetReport.totalOvertimeHours}</td>
                  <td>{timesheetReport.totalAllowance}</td>
                  <td>{timesheetReport.status}</td>
                  <td>
                    {timesheetReport.generatedAt ? (
                      <TextFormat type="date" value={timesheetReport.generatedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {timesheetReport.approvedAt ? (
                      <TextFormat type="date" value={timesheetReport.approvedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{timesheetReport.comments}</td>
                  <td>{timesheetReport.generatedBy ? timesheetReport.generatedBy.login : ''}</td>
                  <td>{timesheetReport.approvedBy ? timesheetReport.approvedBy.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/timesheet-report/${timesheetReport.id}`}
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
                        to={`/timesheet-report/${timesheetReport.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                        onClick={() =>
                          (window.location.href = `/timesheet-report/${timesheetReport.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
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
              <Translate contentKey="timesheetSystemApp.timesheetReport.home.notFound">No Timesheet Reports found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={timesheetReportList && timesheetReportList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default TimesheetReport;
