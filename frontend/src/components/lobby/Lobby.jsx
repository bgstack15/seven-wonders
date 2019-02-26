//@flow
import { Button, Classes, Intent } from '@blueprintjs/core';
import { List } from 'immutable';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import type { Game } from '../../models/games';
import type { Player } from '../../models/players';
import { actions } from '../../redux/actions/lobby';
import { getCurrentGame } from '../../redux/games';
import { getCurrentPlayer, getPlayers } from '../../redux/players';
import { RadialPlayerList } from './RadialPlayerList';

export type LobbyProps = {
  currentGame: Game,
  currentPlayer: Player,
  players: List<Player>,
  startGame: () => void,
}

class LobbyPresenter extends Component<LobbyProps> {

  render() {
    const {currentGame, currentPlayer, players, startGame} = this.props;
    return (
      <div>
        <h2>{currentGame.name + ' — Lobby'}</h2>
        <RadialPlayerList players={players} owner={currentGame.owner} currentPlayer={currentPlayer}/>
        {currentPlayer.gameOwner && <Button text="START" className={Classes.LARGE} intent={Intent.PRIMARY} icon='play'
                onClick={startGame} disabled={players.size < 3}/>}
      </div>
    );
  }
}

const mapStateToProps = state => {
  const game = getCurrentGame(state.get('games'));
  console.info(game);
  return {
    currentGame: game,
    currentPlayer: getCurrentPlayer(state),
    players: game ? getPlayers(state.get('players'), game.players) : new List(),
  };
};

const mapDispatchToProps = {
  startGame: actions.requestStartGame,
};

export const Lobby = connect(mapStateToProps, mapDispatchToProps)(LobbyPresenter);
