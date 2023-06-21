import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './order.reducer';

export const OrderDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const orderEntity = useAppSelector(state => state.order.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="orderDetailsHeading">
          <Translate contentKey="bookingCareV3App.order.detail.title">Order</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{orderEntity.id}</dd>
          <dt>
            <span id="address">
              <Translate contentKey="bookingCareV3App.order.address">Address</Translate>
            </span>
          </dt>
          <dd>{orderEntity.address}</dd>
          <dt>
            <span id="symptom">
              <Translate contentKey="bookingCareV3App.order.symptom">Symptom</Translate>
            </span>
          </dt>
          <dd>{orderEntity.symptom}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="bookingCareV3App.order.date">Date</Translate>
            </span>
          </dt>
          <dd>{orderEntity.date ? <TextFormat value={orderEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="bookingCareV3App.order.status">Status</Translate>
            </span>
          </dt>
          <dd>{orderEntity.status}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="bookingCareV3App.order.price">Price</Translate>
            </span>
          </dt>
          <dd>{orderEntity.price}</dd>
          <dt>
            <Translate contentKey="bookingCareV3App.order.timeslot">Timeslot</Translate>
          </dt>
          <dd>{orderEntity.timeslot ? orderEntity.timeslot.id : ''}</dd>
          <dt>
            <Translate contentKey="bookingCareV3App.order.customer">Customer</Translate>
          </dt>
          <dd>{orderEntity.customer ? orderEntity.customer.id : ''}</dd>
          <dt>
            <Translate contentKey="bookingCareV3App.order.doctor">Doctor</Translate>
          </dt>
          <dd>{orderEntity.doctor ? orderEntity.doctor.id : ''}</dd>
          <dt>
            <Translate contentKey="bookingCareV3App.order.pack">Pack</Translate>
          </dt>
          <dd>{orderEntity.pack ? orderEntity.pack.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/order" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order/${orderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrderDetail;
