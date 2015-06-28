package com.github.iamasoft.tests.firmadvisor.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Nonnull;

import com.github.iamasoft.tests.firmadvisor.ApplicationException;
import com.github.iamasoft.tests.firmadvisor.twogis.TwoGisClient;

/**
 * Implementation of a {@link IFirmAdvisor} that finds {@link AdvisedFirm}s in every given city in parallel.
 *
 * @author Kirill V. Karavaev
 */
public class ParallelTwoGisFirmAdvisor extends AbstractTwoGisFirmAdvisor {

	/** Parallel tasks executor. */
	@Nonnull
	private final ExecutorService executor;

	/**
	 * Constructs 2GIS firm advisor.
	 * @param apiKey
	 *        2GIS API access key
	 */
	public ParallelTwoGisFirmAdvisor(String apiKey) {
		super(apiKey);
		this.executor = Executors.newCachedThreadPool();
	}

	/**
	 * Invokes {@link AdvisedFirmFinder} in parallel and then collects the results.
	 * @throws ApplicationException
	 *         if any of the finder invocation fails
	 */
	@Override
	protected List<AdvisedFirm> advise(TwoGisClient twoGisClient, String category, Collection<String> cities)
			throws ApplicationException {
		// Collecting parallel tasks
		Collection<AdvisedFirmFinder> finders = new ArrayList<>(cities.size());
		for (String city : cities) {
			finders.add(new AdvisedFirmFinder(twoGisClient, city, category));
		}
		// Running the tasks and collecting results
		List<AdvisedFirm> firms = new ArrayList<>(cities.size());
		try {
			for (Future<AdvisedFirm> task : executor.invokeAll(finders)) {
				AdvisedFirm firm = task.get();
				if (firm != null) {
					firms.add(firm);
				}
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return firms;
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();
			if (cause instanceof ApplicationException) {
				throw (ApplicationException) cause;
			} else {
				throw new ApplicationException("Unexpected error during parallel task execution", cause);
			}
		}
		// Ready
		return firms;
	}

}
