import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hospital.reducer';

export const HospitalDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hospitalEntity = useAppSelector(state => state.hospital.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hospitalDetailsHeading">
          <Translate contentKey="bookingCareV3App.hospital.detail.title">Hospital</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hospitalEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="bookingCareV3App.hospital.name">Name</Translate>
            </span>
          </dt>
          <dd>{hospitalEntity.name}</dd>
          <dt>
            <span id="address">
              <Translate contentKey="bookingCareV3App.hospital.address">Address</Translate>
            </span>
          </dt>
          <dd>{hospitalEntity.address}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="bookingCareV3App.hospital.email">Email</Translate>
            </span>
          </dt>
          <dd>{hospitalEntity.email}</dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="bookingCareV3App.hospital.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{hospitalEntity.phoneNumber}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="bookingCareV3App.hospital.description">Description</Translate>
            </span>
          </dt>
          <dd>{hospitalEntity.description}</dd>
          <dt>
            <span id="workDay">
              <Translate contentKey="bookingCareV3App.hospital.workDay">Work Day</Translate>
            </span>
          </dt>
          <dd>{hospitalEntity.workDay}</dd>
          <dt>
            <span id="workTime">
              <Translate contentKey="bookingCareV3App.hospital.workTime">Work Time</Translate>
            </span>
          </dt>
          <dd>{hospitalEntity.workTime}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="bookingCareV3App.hospital.type">Type</Translate>
            </span>
          </dt>
          <dd>{hospitalEntity.type}</dd>
          <dt>
            <span id="procedure">
              <Translate contentKey="bookingCareV3App.hospital.procedure">Procedure</Translate>
            </span>
          </dt>
          <dd>{hospitalEntity.procedure}</dd>
        </dl>
        <Button tag={Link} to="/hospital" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hospital/${hospitalEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HospitalDetail;
