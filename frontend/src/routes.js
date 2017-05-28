import { fork } from 'redux-saga/effects';
import homeSaga from './sagas/home';
import gameBrowserSaga from './sagas/gameBrowser';
import lobbySaga from './sagas/lobby';

import { HomeLayout, LobbyLayout } from './layouts';
import HomePage from './containers/home';
import GameBrowser from './containers/gameBrowser';
import Lobby from './containers/lobby';
import Error404 from './components/errors/Error404';

export const makeSagaRoutes = wsConnection => ({
  *'/'() {
    yield fork(homeSaga, wsConnection);
  },
  *'/games'() {
    yield fork(gameBrowserSaga, wsConnection);
  },
  *'/lobby/*'() {
    yield fork(lobbySaga, wsConnection);
  },
});

export const routes = [
  {
    path: '/',
    component: HomeLayout,
    indexRoute: { component: HomePage },
  },
  {
    path: '/',
    component: LobbyLayout,
    childRoutes: [{ path: '/games', component: GameBrowser }, { path: '/lobby/*', component: Lobby }],
  },
  {
    path: '*',
    component: Error404,
  },
];
