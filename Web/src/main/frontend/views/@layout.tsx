import { createMenuItems, useViewConfig } from '@vaadin/hilla-file-router/runtime.js';
import { effect, signal, useSignal } from '@vaadin/hilla-react-signals';
import { AppLayout, DrawerToggle, Icon, SideNav, SideNavItem, Avatar } from '@vaadin/react-components';
import { Suspense, useEffect } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';
import UserInfo  from "Frontend/generated/com/github/markash/threesixty/web/service/UserInfo";
import { CurrentUserProvider } from "Frontend/generated/endpoints";

const defaultTitle = document.title;
const documentTitleSignal = signal('');
effect(() => {
  document.title = documentTitleSignal.value;
});

// Publish for Vaadin to use
(window as any).Vaadin.documentTitleSignal = documentTitleSignal;

export default function MainLayout() {
  const currentTitle = useViewConfig()?.title ?? defaultTitle;
  const navigate = useNavigate();
  const location = useLocation();
  const person = useSignal<UserInfo | undefined>(undefined)
  
  async function updatePerson() {
    person.value = await CurrentUserProvider.getCurrentUser();
    console.log(person);
  }

  useEffect(() => {
    documentTitleSignal.value = currentTitle;
    updatePerson();
  }, [currentTitle]);

  return (
    <AppLayout primarySection="drawer">
      <div slot="drawer" className="flex flex-col justify-between h-full p-m">
        <header className="flex flex-col gap-m">
          <span className="text-xxxl text-center">Three<span className="font-semibold">Sixty</span></span>
          <span className="text-center">
              <Avatar
                name={`${person.value?.firstName} ${person.value?.lastName}`}
                theme="xlarge" />
              <br />
              <span className="text-center">{`${person.value?.firstName} ${person.value?.lastName}`}</span>
          </span>
          <SideNav onNavigate={({ path }) => navigate(path!)} location={location}>
            {createMenuItems().map(({ to, title, icon }) => (
              <SideNavItem path={to} key={to}>
                {icon ? <Icon src={icon} slot="prefix"></Icon> : <></>}
                {title}
              </SideNavItem>
            ))}
          </SideNav>
        </header>
      </div>

      <DrawerToggle slot="navbar" aria-label="Menu toggle"></DrawerToggle>
      <h1 slot="navbar" className="text-l m-0">
        {documentTitleSignal}
      </h1>

      <Suspense>
        <Outlet />
      </Suspense>
    </AppLayout>
  );
}
