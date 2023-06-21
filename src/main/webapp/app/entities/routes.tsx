import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Hospital from './hospital';
import Doctor from './doctor';
import TimeSlot from './time-slot';
import Department from './department';
import Pack from './pack';
import Order from './order';
import Customer from './customer';
import Diagnose from './diagnose';
import Image from './image';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="hospital/*" element={<Hospital />} />
        <Route path="doctor/*" element={<Doctor />} />
        <Route path="time-slot/*" element={<TimeSlot />} />
        <Route path="department/*" element={<Department />} />
        <Route path="pack/*" element={<Pack />} />
        <Route path="order/*" element={<Order />} />
        <Route path="customer/*" element={<Customer />} />
        <Route path="diagnose/*" element={<Diagnose />} />
        <Route path="image/*" element={<Image />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
