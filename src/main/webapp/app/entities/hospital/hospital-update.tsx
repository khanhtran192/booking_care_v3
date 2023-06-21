import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IHospital } from 'app/shared/model/hospital.model';
import { FacilityType } from 'app/shared/model/enumerations/facility-type.model';
import { getEntity, updateEntity, createEntity, reset } from './hospital.reducer';

export const HospitalUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const hospitalEntity = useAppSelector(state => state.hospital.entity);
  const loading = useAppSelector(state => state.hospital.loading);
  const updating = useAppSelector(state => state.hospital.updating);
  const updateSuccess = useAppSelector(state => state.hospital.updateSuccess);
  const facilityTypeValues = Object.keys(FacilityType);

  const handleClose = () => {
    navigate('/hospital' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...hospitalEntity,
      ...values,
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
          type: 'BENH_VIEN',
          ...hospitalEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bookingCareV3App.hospital.home.createOrEditLabel" data-cy="HospitalCreateUpdateHeading">
            <Translate contentKey="bookingCareV3App.hospital.home.createOrEditLabel">Create or edit a Hospital</Translate>
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
                  id="hospital-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bookingCareV3App.hospital.name')}
                id="hospital-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('bookingCareV3App.hospital.address')}
                id="hospital-address"
                name="address"
                data-cy="address"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('bookingCareV3App.hospital.email')}
                id="hospital-email"
                name="email"
                data-cy="email"
                type="text"
              />
              <ValidatedField
                label={translate('bookingCareV3App.hospital.phoneNumber')}
                id="hospital-phoneNumber"
                name="phoneNumber"
                data-cy="phoneNumber"
                type="text"
              />
              <ValidatedField
                label={translate('bookingCareV3App.hospital.description')}
                id="hospital-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('bookingCareV3App.hospital.workDay')}
                id="hospital-workDay"
                name="workDay"
                data-cy="workDay"
                type="text"
              />
              <ValidatedField
                label={translate('bookingCareV3App.hospital.workTime')}
                id="hospital-workTime"
                name="workTime"
                data-cy="workTime"
                type="text"
              />
              <ValidatedField
                label={translate('bookingCareV3App.hospital.type')}
                id="hospital-type"
                name="type"
                data-cy="type"
                type="select"
              >
                {facilityTypeValues.map(facilityType => (
                  <option value={facilityType} key={facilityType}>
                    {translate('bookingCareV3App.FacilityType.' + facilityType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('bookingCareV3App.hospital.procedure')}
                id="hospital-procedure"
                name="procedure"
                data-cy="procedure"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/hospital" replace color="info">
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

export default HospitalUpdate;
