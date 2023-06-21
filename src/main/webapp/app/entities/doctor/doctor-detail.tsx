import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './doctor.reducer';

export const DoctorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const doctorEntity = useAppSelector(state => state.doctor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="doctorDetailsHeading">
          <Translate contentKey="bookingCareV3App.doctor.detail.title">Doctor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{doctorEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="bookingCareV3App.doctor.name">Name</Translate>
            </span>
          </dt>
          <dd>{doctorEntity.name}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="bookingCareV3App.doctor.email">Email</Translate>
            </span>
          </dt>
          <dd>{doctorEntity.email}</dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="bookingCareV3App.doctor.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{doctorEntity.phoneNumber}</dd>
          <dt>
            <span id="dateOfBirth">
              <Translate contentKey="bookingCareV3App.doctor.dateOfBirth">Date Of Birth</Translate>
            </span>
          </dt>
          <dd>{doctorEntity.dateOfBirth ? <TextFormat value={doctorEntity.dateOfBirth} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="hospitalId">
              <Translate contentKey="bookingCareV3App.doctor.hospitalId">Hospital Id</Translate>
            </span>
          </dt>
          <dd>{doctorEntity.hospitalId}</dd>
          <dt>
            <span id="degree">
              <Translate contentKey="bookingCareV3App.doctor.degree">Degree</Translate>
            </span>
          </dt>
          <dd>{doctorEntity.degree}</dd>
          <dt>
            <span id="rate">
              <Translate contentKey="bookingCareV3App.doctor.rate">Rate</Translate>
            </span>
          </dt>
          <dd>{doctorEntity.rate}</dd>
          <dt>
            <span id="specialize">
              <Translate contentKey="bookingCareV3App.doctor.specialize">Specialize</Translate>
            </span>
          </dt>
          <dd>{doctorEntity.specialize}</dd>
          <dt>
            <Translate contentKey="bookingCareV3App.doctor.department">Department</Translate>
          </dt>
          <dd>{doctorEntity.department ? doctorEntity.department.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/doctor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/doctor/${doctorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DoctorDetail;
