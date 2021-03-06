package com.containersol.minimesos.container;

import com.containersol.minimesos.cluster.MesosCluster;
import com.containersol.minimesos.mesos.MesosAgentContainer;
import com.containersol.minimesos.mesos.MesosClusterContainersFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ContainerNameTest {

    private MesosCluster cluster;
    private String clusterId;

    @Before
    public void before() {
        cluster = new MesosClusterContainersFactory().createMesosCluster("src/test/resources/configFiles/minimesosFile-defaultMinimumClusterTest");

        clusterId = cluster.getClusterId();
    }

    @Test
    public void testBelongsToCluster() throws Exception {
        MesosAgentContainer agent = new MesosAgentContainer(cluster, "UUID", "CONTAINERID");
        String containerName = ContainerName.get(agent);

        assertTrue(ContainerName.hasRoleInCluster(containerName, clusterId, agent.getRole()));
        assertTrue(ContainerName.belongsToCluster(containerName, clusterId));
    }

    @Test
    public void testWrongCluster() throws Exception {
        MesosAgentContainer agent = new MesosAgentContainer(cluster, "UUID", "CONTAINERID");
        String containerName = ContainerName.get(agent);

        assertFalse(ContainerName.hasRoleInCluster(containerName, "XXXXXX", agent.getRole()));
        assertFalse(ContainerName.belongsToCluster(containerName, "XXXXXX"));
    }

    @Test
    public void testWrongRole() throws Exception {
        MesosAgentContainer agent = new MesosAgentContainer(cluster, "UUID", "CONTAINERID");
        String containerName = ContainerName.get(agent);

        assertFalse(ContainerName.hasRoleInCluster(containerName, clusterId, "XXXXXX"));
        assertTrue(ContainerName.belongsToCluster(containerName, clusterId));
    }

    @Test
    public void testSimpleContainerName() {
        String[] names = new String[1];
        names[0] = "/minimesos-agent";

        assertEquals("minimesos-agent", ContainerName.getFromDockerNames(names));
    }

    @Test
    public void testLinkedContainerNames() {
        String[] names = new String[4];
        names[0] = "/minimesos-agent0/minimesos-zookeeper";
        names[1] = "/minimesos-agent1/minimesos-zookeeper";
        names[2] = "/minimesos-agent2/minimesos-zookeeper";
        names[3] = "/minimesos-zookeeper";

        assertEquals("minimesos-zookeeper", ContainerName.getFromDockerNames(names));
    }

}
