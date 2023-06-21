import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './diagnose.reducer';

export const DiagnoseDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const diagnoseEntity = useAppSelector(state => state.diagnose.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="diagnoseDetailsHeading">
          <Translate contentKey="bookingCareV3App.diagnose.detail.title">Diagnose</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{diagnoseEntity.id}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="bookingCareV3App.diagnose.description">Description</Translate>
            </span>
          </dt>
          <dd>{diagnoseEntity.description}</dd>
          <dt>
            <Translate contentKey="bookingCareV3App.diagnose.order">Order</Translate>
          </dt>
          <dd>{diagnoseEntity.order ? diagnoseEntity.order.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/diagnose" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/diagnose/${diagnoseEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DiagnoseDetail;
