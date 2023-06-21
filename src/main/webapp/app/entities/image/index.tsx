import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Image from './image';
import ImageDetail from './image-detail';
import ImageUpdate from './image-update';
import ImageDeleteDialog from './image-delete-dialog';

const ImageRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Image />} />
    <Route path="new" element={<ImageUpdate />} />
    <Route path=":id">
      <Route index element={<ImageDetail />} />
      <Route path="edit" element={<ImageUpdate />} />
      <Route path="delete" element={<ImageDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ImageRoutes;
