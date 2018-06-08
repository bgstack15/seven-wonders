// @flow
import { Button, Classes, InputGroup, Intent, Text } from '@blueprintjs/core';
import type { List } from 'immutable';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Flex } from 'reflexbox';
import { GameList } from '../../components/gameList';
import type { Game } from '../../models/games';
import type { Player } from '../../models/players';
import { actions, getAllGames } from '../../redux/games';
import { getCurrentPlayer } from '../../redux/players';

export type GameBrowserProps = {
  currentPlayer: Player,
  games: List<Game>,
  createGame: (gameName: string) => void,
  joinGame: (gameId: string) => void
}

class GameBrowserPresenter extends Component<GameBrowserProps> {
  props: {
    currentPlayer: Player,
    games: List<Game>,
    createGame: (gameName: string) => void,
    joinGame: (gameId: string) => void
  };

  _gameName: string | void = undefined;

  createGame = (e: SyntheticEvent<*>): void => {
    e.preventDefault();
    if (this._gameName !== undefined) {
      this.props.createGame(this._gameName);
    }
  };

  render() {
    return (
      <div>
        <Flex align="center" p={1}>
          <InputGroup
                  placeholder="Game name"
                  name="game_name"
                  onChange={(e: SyntheticInputEvent<*>) => (this._gameName = e.target.value)}
                  rightElement={<CreateGameButton onClick={this.createGame}/>}
          />
          <Text>
            <b>Username:</b>
            {' '}
            {this.props.currentPlayer && this.props.currentPlayer.displayName}
          </Text>
        </Flex>
        <GameList games={this.props.games} joinGame={this.props.joinGame} />
      </div>
    );
  }
}

const CreateGameButton = ({onClick}) => (
  <Button className={Classes.MINIMAL} onClick={onClick} intent={Intent.PRIMARY}>Create Game</Button>
);

const mapStateToProps = state => ({
  currentPlayer: getCurrentPlayer(state.get('players')),
  games: getAllGames(state.get('games')),
});

const mapDispatchToProps = {
  createGame: actions.requestCreateGame,
  joinGame: actions.requestJoinGame,
};

export const GameBrowser = connect(mapStateToProps, mapDispatchToProps)(GameBrowserPresenter);