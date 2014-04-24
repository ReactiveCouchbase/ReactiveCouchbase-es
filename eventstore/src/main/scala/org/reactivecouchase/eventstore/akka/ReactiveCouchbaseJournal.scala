package org.reactivecouchase.eventstore.akka

import akka.persistence.journal.AsyncWriteJournal
import scala.concurrent.Future
import scala.collection.immutable.Seq
import akka.persistence.{PersistentRepr, PersistentConfirmation, PersistentId}

class ReactiveCouchbaseJournal extends AsyncWriteJournal {

  def asyncReplayMessages(processorId: String, fromSequenceNr: Long, toSequenceNr: Long, max: Long)(replayCallback: (PersistentRepr) => Unit): Future[Unit] = {

    ???
  }

  def asyncReadHighestSequenceNr(processorId: String, fromSequenceNr: Long): Future[Long] = {

    ???
  }

  def asyncWriteMessages(messages: Seq[PersistentRepr]): Future[Unit] = {
    messages.foreach(m => m.)

    ???
  }

  def asyncWriteConfirmations(confirmations: Seq[PersistentConfirmation]): Future[Unit] = {
    confirmations.foreach(c => c.)
    ???
  }

  def asyncDeleteMessages(messageIds: Seq[PersistentId], permanent: Boolean): Future[Unit] = {

    ???
  }

  def asyncDeleteMessagesTo(processorId: String, toSequenceNr: Long, permanent: Boolean): Future[Unit] = {

    ???
  }
}
