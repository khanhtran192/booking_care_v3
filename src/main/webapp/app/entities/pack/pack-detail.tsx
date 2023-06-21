import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pack.reducer';

export const PackDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const packEntity = useAppSelector(state => state.pack.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="packDetailsHeading">
          <Translate contentKey="bookingCareV3App.pack.detail.title">Pack</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{packEntity.id}</dd>
          <dt>
            <span id="nane">
              <Translate contentKey="bookingCareV3App.pack.nane">Nane</Translate>
            </span>
          </dt>
          <dd>{packEntity.nane}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="bookingCareV3App.pack.description">Description</Translate>
            </span>
          </dt>
          <dd>{packEntity.description}</dd>
          <dt>
            <Translate contentKey="bookingCareV3App.pack.hospital">Hospital</Translate>
          </dt>
          <dd>{packEntity.hospital ? packEntity.hospital.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/pack" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pack/${packEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PackDetail;
