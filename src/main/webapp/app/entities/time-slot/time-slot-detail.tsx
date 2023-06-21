import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './time-slot.reducer';

export const TimeSlotDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const timeSlotEntity = useAppSelector(state => state.timeSlot.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="timeSlotDetailsHeading">
          <Translate contentKey="bookingCareV3App.timeSlot.detail.title">TimeSlot</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{timeSlotEntity.id}</dd>
          <dt>
            <span id="time">
              <Translate contentKey="bookingCareV3App.timeSlot.time">Time</Translate>
            </span>
          </dt>
          <dd>{timeSlotEntity.time}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="bookingCareV3App.timeSlot.description">Description</Translate>
            </span>
          </dt>
          <dd>{timeSlotEntity.description}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="bookingCareV3App.timeSlot.price">Price</Translate>
            </span>
          </dt>
          <dd>{timeSlotEntity.price}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="bookingCareV3App.timeSlot.status">Status</Translate>
            </span>
          </dt>
          <dd>{timeSlotEntity.status ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="bookingCareV3App.timeSlot.doctor">Doctor</Translate>
          </dt>
          <dd>{timeSlotEntity.doctor ? timeSlotEntity.doctor.id : ''}</dd>
          <dt>
            <Translate contentKey="bookingCareV3App.timeSlot.pack">Pack</Translate>
          </dt>
          <dd>{timeSlotEntity.pack ? timeSlotEntity.pack.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/time-slot" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/time-slot/${timeSlotEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TimeSlotDetail;
