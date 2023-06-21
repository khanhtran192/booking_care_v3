import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Pack from './pack';
import PackDetail from './pack-detail';
import PackUpdate from './pack-update';
import PackDeleteDialog from './pack-delete-dialog';

const PackRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Pack />} />
    <Route path="new" element={<PackUpdate />} />
    <Route path=":id">
      <Route index element={<PackDetail />} />
      <Route path="edit" element={<PackUpdate />} />
      <Route path="delete" element={<PackDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PackRoutes;
