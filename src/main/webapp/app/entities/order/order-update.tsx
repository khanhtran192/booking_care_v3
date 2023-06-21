import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITimeSlot } from 'app/shared/model/time-slot.model';
import { getEntities as getTimeSlots } from 'app/entities/time-slot/time-slot.reducer';
import { ICustomer } from 'app/shared/model/customer.model';
import { getEntities as getCustomers } from 'app/entities/customer/customer.reducer';
import { IDoctor } from 'app/shared/model/doctor.model';
import { getEntities as getDoctors } from 'app/entities/doctor/doctor.reducer';
import { IPack } from 'app/shared/model/pack.model';
import { getEntities as getPacks } from 'app/entities/pack/pack.reducer';
import { IOrder } from 'app/shared/model/order.model';
import { OrderStatus } from 'app/shared/model/enumerations/order-status.model';
import { getEntity, updateEntity, createEntity, reset } from './order.reducer';

export const OrderUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const timeSlots = useAppSelector(state => state.timeSlot.entities);
  const customers = useAppSelector(state => state.customer.entities);
  const doctors = useAppSelector(state => state.doctor.entities);
  const packs = useAppSelector(state => state.pack.entities);
  const orderEntity = useAppSelector(state => state.order.entity);
  const loading = useAppSelector(state => state.order.loading);
  const updating = useAppSelector(state => state.order.updating);
  const updateSuccess = useAppSelector(state => state.order.updateSuccess);
  const orderStatusValues = Object.keys(OrderStatus);

  const handleClose = () => {
    navigate('/order' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTimeSlots({}));
    dispatch(getCustomers({}));
    dispatch(getDoctors({}));
    dispatch(getPacks({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.date = convertDateTimeToServer(values.date);

    const entity = {
      ...orderEntity,
      ...values,
      timeslot: timeSlots.find(it => it.id.toString() === values.timeslot.toString()),
      customer: customers.find(it => it.id.toString() === values.customer.toString()),
      doctor: doctors.find(it => it.id.toString() === values.doctor.toString()),
      pack: packs.find(it => it.id.toString() === values.pack.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          date: displayDefaultDateTime(),
        }
      : {
          status: 'PENDING',
          ...orderEntity,
          date: convertDateTimeFromServer(orderEntity.date),
          timeslot: orderEntity?.timeslot?.id,
          customer: orderEntity?.customer?.id,
          doctor: orderEntity?.doctor?.id,
          pack: orderEntity?.pack?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bookingCareV3App.order.home.createOrEditLabel" data-cy="OrderCreateUpdateHeading">
            <Translate contentKey="bookingCareV3App.order.home.createOrEditLabel">Create or edit a Order</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="order-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bookingCareV3App.order.address')}
                id="order-address"
                name="address"
                data-cy="address"
                type="text"
              />
              <ValidatedField
                label={translate('bookingCareV3App.order.symptom')}
                id="order-symptom"
                name="symptom"
                data-cy="symptom"
                type="text"
              />
              <ValidatedField
                label={translate('bookingCareV3App.order.date')}
                id="order-date"
                name="date"
                data-cy="date"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('bookingCareV3App.order.status')}
                id="order-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {orderStatusValues.map(orderStatus => (
                  <option value={orderStatus} key={orderStatus}>
                    {translate('bookingCareV3App.OrderStatus.' + orderStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label={translate('bookingCareV3App.order.price')} id="order-price" name="price" data-cy="price" type="text" />
              <ValidatedField
                id="order-timeslot"
                name="timeslot"
                data-cy="timeslot"
                label={translate('bookingCareV3App.order.timeslot')}
                type="select"
              >
                <option value="" key="0" />
                {timeSlots
                  ? timeSlots.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="order-customer"
                name="customer"
                data-cy="customer"
                label={translate('bookingCareV3App.order.customer')}
                type="select"
              >
                <option value="" key="0" />
                {customers
                  ? customers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="order-doctor"
                name="doctor"
                data-cy="doctor"
                label={translate('bookingCareV3App.order.doctor')}
                type="select"
              >
                <option value="" key="0" />
                {doctors
                  ? doctors.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="order-pack" name="pack" data-cy="pack" label={translate('bookingCareV3App.order.pack')} type="select">
                <option value="" key="0" />
                {packs
                  ? packs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/order" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default OrderUpdate;
