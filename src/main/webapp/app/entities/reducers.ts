import hospital from 'app/entities/hospital/hospital.reducer';
import doctor from 'app/entities/doctor/doctor.reducer';
import timeSlot from 'app/entities/time-slot/time-slot.reducer';
import department from 'app/entities/department/department.reducer';
import pack from 'app/entities/pack/pack.reducer';
import order from 'app/entities/order/order.reducer';
import customer from 'app/entities/customer/customer.reducer';
import diagnose from 'app/entities/diagnose/diagnose.reducer';
import image from 'app/entities/image/image.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  hospital,
  doctor,
  timeSlot,
  department,
  pack,
  order,
  customer,
  diagnose,
  image,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
