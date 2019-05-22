import React from 'react';
import { ApiBoard, ApiTableCard, ApiWonder } from '../../api/model';
import './Board.css'
import { CardImage } from './CardImage';

// card offsets in % of their size when displayed in columns
const xOffset = 20;
const yOffset = 21;

type BoardProps = {
  board: ApiBoard,
}

export const Board = ({board}: BoardProps) => {
  return <div className='board'>
    <TableCards cardColumns={board.playedCards}/>
    <Wonder wonder={board.wonder}/>
  </div>;
};

type TableCardsProps = {
  cardColumns: ApiTableCard[][],
}

const TableCards = ({cardColumns}: TableCardsProps) => {
  return <div className="cards">
    {cardColumns.map(column => <TableCardColumn key={column[0].color} cards={column}/>)}
  </div>
};

type TableCardColumnProps = {
  cards: ApiTableCard[]
}

const TableCardColumn = ({cards}: TableCardColumnProps) => {
  return <div className="card-column">
    {cards.map((c, i) => <TableCard key={c.name} card={c} indexInColumn={i}/>)}
  </div>
};

type TableCardProps = {
  card: ApiTableCard,
  indexInColumn: number,
}

const TableCard = ({card, indexInColumn}: TableCardProps) => {
  let style = {
    transform: `translate(${indexInColumn * xOffset}%, ${indexInColumn * yOffset}%)`,
    zIndex: indexInColumn,
  };
  return <div className="card" style={style}>
    <CardImage card={card} otherClasses="table-card-img"/>
  </div>
};

type WonderProps = {
  wonder: ApiWonder,
}

const Wonder = ({wonder}: WonderProps) => {
  return <div className="wonder">
    <img src={`/images/wonders/${wonder.image}`}
         title={wonder.name}
         alt={`Wonder ${wonder.name}`}
         className="wonder-img"/>
  </div>
};