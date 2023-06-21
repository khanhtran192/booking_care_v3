import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Doctor from './doctor';
import DoctorDetail from './doctor-detail';
import DoctorUpdate from './doctor-update';
import DoctorDeleteDialog from './doctor-delete-dialog';

const DoctorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Doctor />} />
    <Route path="new" element={<DoctorUpdate />} />
    <Route path=":id">
      <Route index element={<DoctorDetail />} />
      <Route path="edit" element={<DoctorUpdate />} />
      <Route path="delete" element={<DoctorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DoctorRoutes;
