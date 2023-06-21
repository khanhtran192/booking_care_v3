import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Diagnose from './diagnose';
import DiagnoseDetail from './diagnose-detail';
import DiagnoseUpdate from './diagnose-update';
import DiagnoseDeleteDialog from './diagnose-delete-dialog';

const DiagnoseRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Diagnose />} />
    <Route path="new" element={<DiagnoseUpdate />} />
    <Route path=":id">
      <Route index element={<DiagnoseDetail />} />
      <Route path="edit" element={<DiagnoseUpdate />} />
      <Route path="delete" element={<DiagnoseDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DiagnoseRoutes;
