import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IHospital } from 'app/shared/model/hospital.model';
import { getEntities as getHospitals } from 'app/entities/hospital/hospital.reducer';
import { IPack } from 'app/shared/model/pack.model';
import { getEntity, updateEntity, createEntity, reset } from './pack.reducer';

export const PackUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const hospitals = useAppSelector(state => state.hospital.entities);
  const packEntity = useAppSelector(state => state.pack.entity);
  const loading = useAppSelector(state => state.pack.loading);
  const updating = useAppSelector(state => state.pack.updating);
  const updateSuccess = useAppSelector(state => state.pack.updateSuccess);

  const handleClose = () => {
    navigate('/pack' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getHospitals({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...packEntity,
      ...values,
      hospital: hospitals.find(it => it.id.toString() === values.hospital.toString()),
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
          ...packEntity,
          hospital: packEntity?.hospital?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bookingCareV3App.pack.home.createOrEditLabel" data-cy="PackCreateUpdateHeading">
            <Translate contentKey="bookingCareV3App.pack.home.createOrEditLabel">Create or edit a Pack</Translate>
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
                  id="pack-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('bookingCareV3App.pack.nane')} id="pack-nane" name="nane" data-cy="nane" type="text" />
              <ValidatedField
                label={translate('bookingCareV3App.pack.description')}
                id="pack-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                id="pack-hospital"
                name="hospital"
                data-cy="hospital"
                label={translate('bookingCareV3App.pack.hospital')}
                type="select"
              >
                <option value="" key="0" />
                {hospitals
                  ? hospitals.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/pack" replace color="info">
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

export default PackUpdate;
