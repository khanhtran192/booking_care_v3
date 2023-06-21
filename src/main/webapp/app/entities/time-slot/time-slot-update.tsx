import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDoctor } from 'app/shared/model/doctor.model';
import { getEntities as getDoctors } from 'app/entities/doctor/doctor.reducer';
import { IPack } from 'app/shared/model/pack.model';
import { getEntities as getPacks } from 'app/entities/pack/pack.reducer';
import { ITimeSlot } from 'app/shared/model/time-slot.model';
import { getEntity, updateEntity, createEntity, reset } from './time-slot.reducer';

export const TimeSlotUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const doctors = useAppSelector(state => state.doctor.entities);
  const packs = useAppSelector(state => state.pack.entities);
  const timeSlotEntity = useAppSelector(state => state.timeSlot.entity);
  const loading = useAppSelector(state => state.timeSlot.loading);
  const updating = useAppSelector(state => state.timeSlot.updating);
  const updateSuccess = useAppSelector(state => state.timeSlot.updateSuccess);

  const handleClose = () => {
    navigate('/time-slot' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getDoctors({}));
    dispatch(getPacks({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...timeSlotEntity,
      ...values,
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
      ? {}
      : {
          ...timeSlotEntity,
          doctor: timeSlotEntity?.doctor?.id,
          pack: timeSlotEntity?.pack?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bookingCareV3App.timeSlot.home.createOrEditLabel" data-cy="TimeSlotCreateUpdateHeading">
            <Translate contentKey="bookingCareV3App.timeSlot.home.createOrEditLabel">Create or edit a TimeSlot</Translate>
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
                  id="time-slot-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bookingCareV3App.timeSlot.time')}
                id="time-slot-time"
                name="time"
                data-cy="time"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('bookingCareV3App.timeSlot.description')}
                id="time-slot-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('bookingCareV3App.timeSlot.price')}
                id="time-slot-price"
                name="price"
                data-cy="price"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('bookingCareV3App.timeSlot.status')}
                id="time-slot-status"
                name="status"
                data-cy="status"
                check
                type="checkbox"
              />
              <ValidatedField
                id="time-slot-doctor"
                name="doctor"
                data-cy="doctor"
                label={translate('bookingCareV3App.timeSlot.doctor')}
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
              <ValidatedField
                id="time-slot-pack"
                name="pack"
                data-cy="pack"
                label={translate('bookingCareV3App.timeSlot.pack')}
                type="select"
              >
                <option value="" key="0" />
                {packs
                  ? packs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/time-slot" replace color="info">
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

export default TimeSlotUpdate;
