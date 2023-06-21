import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/hospital">
        <Translate contentKey="global.menu.entities.hospital" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/doctor">
        <Translate contentKey="global.menu.entities.doctor" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/time-slot">
        <Translate contentKey="global.menu.entities.timeSlot" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/department">
        <Translate contentKey="global.menu.entities.department" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/pack">
        <Translate contentKey="global.menu.entities.pack" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/order">
        <Translate contentKey="global.menu.entities.order" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/customer">
        <Translate contentKey="global.menu.entities.customer" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/diagnose">
        <Translate contentKey="global.menu.entities.diagnose" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/image">
        <Translate contentKey="global.menu.entities.image" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
