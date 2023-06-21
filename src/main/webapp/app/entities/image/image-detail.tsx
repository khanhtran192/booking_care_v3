import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './image.reducer';

export const ImageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const imageEntity = useAppSelector(state => state.image.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="imageDetailsHeading">
          <Translate contentKey="bookingCareV3App.image.detail.title">Image</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{imageEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="bookingCareV3App.image.name">Name</Translate>
            </span>
          </dt>
          <dd>{imageEntity.name}</dd>
          <dt>
            <span id="path">
              <Translate contentKey="bookingCareV3App.image.path">Path</Translate>
            </span>
          </dt>
          <dd>{imageEntity.path}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="bookingCareV3App.image.type">Type</Translate>
            </span>
          </dt>
          <dd>{imageEntity.type}</dd>
          <dt>
            <span id="hospitalId">
              <Translate contentKey="bookingCareV3App.image.hospitalId">Hospital Id</Translate>
            </span>
          </dt>
          <dd>{imageEntity.hospitalId}</dd>
          <dt>
            <span id="doctorId">
              <Translate contentKey="bookingCareV3App.image.doctorId">Doctor Id</Translate>
            </span>
          </dt>
          <dd>{imageEntity.doctorId}</dd>
          <dt>
            <span id="departmentId">
              <Translate contentKey="bookingCareV3App.image.departmentId">Department Id</Translate>
            </span>
          </dt>
          <dd>{imageEntity.departmentId}</dd>
        </dl>
        <Button tag={Link} to="/image" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/image/${imageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ImageDetail;
