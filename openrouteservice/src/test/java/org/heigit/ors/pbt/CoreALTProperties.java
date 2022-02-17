package org.heigit.ors.pbt;

import com.graphhopper.routing.*;
import com.graphhopper.storage.*;
import org.heigit.ors.pbt.GraphHopperDomain.*;
import org.heigit.ors.routing.graphhopper.extensions.core.*;

import net.jqwik.api.*;
import net.jqwik.api.Tuple.*;
import net.jqwik.api.domains.*;

import static org.assertj.core.api.Assertions.*;

// With 1000 tries GraphHopperStorage.create() sometimes runs out of memory.
// Memory leak?
@PropertyDefaults(tries = 100)
@Domain(GraphHopperDomain.class)
class CoreALTProperties {

	// With 1000 tries GraphHopperStorage.create() sometimes runs out of memory.
	// Memory leak?
	@Property
	void route_from_node_to_itself(@ForAll @MaxNodes(100) Tuple2<GraphHopperStorage, Tuple2<Integer, Integer>> routingScenario) {
		GraphHopperStorage graph = routingScenario.get1();
		int node = routingScenario.get2().get1();

		Path path = calculatePath(graph, node, node);

		assertThat(path.getDistance()).isZero();
	}

	@Property
		//@Report(Reporting.GENERATED)
	void routing_distance_between_non_identical_nodes_at_least_0(
		@ForAll @MaxNodes(1000) Tuple2<GraphHopperStorage, Tuple2<Integer, Integer>> routingScenario
	) {
		GraphHopperStorage graph = routingScenario.get1();
		int from = routingScenario.get2().get1();
		int to = routingScenario.get2().get2();

		Path path = calculatePath(graph, from, to);

		assertThat(path.getDistance())
			.describedAs("distance of path  %s->%s", from, to)
			.isGreaterThanOrEqualTo(0.0);
	}

	@Property
	void adding_additional_edge_will_never_increase_routing_distance(
		@ForAll @MaxNodes(10) Tuple2<GraphHopperStorage, Tuple2<Integer, Integer>> routingScenario
	) {
		GraphHopperStorage graph = routingScenario.get1();
		int from = routingScenario.get2().get1();
		int to = routingScenario.get2().get2();

		Path originalPath = calculatePath(graph, from, to);

		GraphHopperStorage clone = cloneGraph(graph);
		clone.edge(from, to, 3.0, true);
		clone.freeze();

		Path pathWithAdditionEdge = calculatePath(clone, from, to);

		assertThat(originalPath.getDistance()).isGreaterThanOrEqualTo(pathWithAdditionEdge.getDistance());
		assertThat(pathWithAdditionEdge.getDistance()).isLessThanOrEqualTo(3.0);
	}

	private GraphHopperStorage cloneGraph(GraphHopperStorage graph) {
		GraphHopperStorage clone = GraphGenerator.createGHStorage();
		graph.copyTo(clone);
		return clone;
	}

	private Path calculatePath(GraphHopperStorage graph, int from, int to) {
		CoreALT coreALTOrg = Algorithms.coreALT(graph, GraphHopperDomain.SHORTEST_WEIGHTING_FOR_CARS);
		return coreALTOrg.calcPath(from, to);
	}
}
