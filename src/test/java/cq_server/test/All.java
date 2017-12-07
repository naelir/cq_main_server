package cq_server.test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;

import cq_server.game.BasePlayer;
import cq_server.game.IdFactory;
import cq_server.game.MainChat;
import cq_server.model.UserList;
 
public class All {
	@Test
	public void idFactory() throws InterruptedException, ExecutionException {
		final IdFactory idFactory = new IdFactory();
		final ExecutorService executorService = Executors.newFixedThreadPool(2);
		final Future<Integer> first = executorService.submit(() -> {
			return idFactory.createId(BasePlayer.class);
		});
		final Future<Integer> second = executorService.submit(() -> {
			return idFactory.createId(MainChat.class);
		});
		second.get();
		final Future<Integer> third = executorService.submit(() -> {
			return idFactory.createId(UserList.class);
		});
		final Future<Integer> fourth = executorService.submit(() -> {
			return idFactory.createId(UserList.class);
		});
		Assert.assertTrue(first.get() == 1);
		Assert.assertTrue(second.get() == 0);
		Assert.assertTrue(third.get() == 0);
		Assert.assertTrue(fourth.get() - third.get() == 1);
		executorService.shutdown();
	}
}
