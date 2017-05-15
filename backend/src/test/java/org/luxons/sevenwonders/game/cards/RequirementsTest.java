package org.luxons.sevenwonders.game.cards;

import java.util.Arrays;
import java.util.Collections;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.luxons.sevenwonders.game.api.Table;
import org.luxons.sevenwonders.game.boards.Board;
import org.luxons.sevenwonders.game.resources.BoughtResources;
import org.luxons.sevenwonders.game.resources.Provider;
import org.luxons.sevenwonders.game.resources.ResourceType;
import org.luxons.sevenwonders.game.test.TestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

@RunWith(Theories.class)
public class RequirementsTest {

    @DataPoints
    public static int[] goldAmounts() {
        return new int[]{0, 1, 2, 5};
    }

    @DataPoints
    public static ResourceType[] resourceTypes() {
        return ResourceType.values();
    }

    @Theory
    public void goldRequirement(int boardGold, int requiredGold) {
        Requirements requirements = new Requirements();
        requirements.setGold(requiredGold);

        Board board = TestUtils.createBoard(ResourceType.CLAY, boardGold);
        Table table = new Table(Collections.singletonList(board));

        assertEquals(boardGold >= requiredGold, requirements.areMetWithoutNeighboursBy(board));
        assertEquals(boardGold >= requiredGold, requirements.areMetWithHelpBy(board, Collections.emptyList()));
        assertEquals(boardGold >= requiredGold, requirements.couldBeMetBy(table, 0));
    }

    @Theory
    public void resourceRequirement_initialResource(ResourceType initialResource, ResourceType requiredResource) {
        Requirements requirements = TestUtils.createRequirements(requiredResource);

        Board board = TestUtils.createBoard(initialResource, 0);
        Table table = new Table(Collections.singletonList(board));

        assertEquals(initialResource == requiredResource, requirements.areMetWithoutNeighboursBy(board));
        assertEquals(initialResource == requiredResource,
                requirements.areMetWithHelpBy(board, Collections.emptyList()));

        if (initialResource == requiredResource) {
            assertTrue(requirements.couldBeMetBy(table, 0));
        }
    }

    @Theory
    public void resourceRequirement_ownProduction(ResourceType initialResource, ResourceType producedResource,
                                                  ResourceType requiredResource) {
        assumeTrue(initialResource != requiredResource);

        Requirements requirements = TestUtils.createRequirements(requiredResource);

        Board board = TestUtils.createBoard(initialResource, 0);
        board.getProduction().addFixedResource(producedResource, 1);
        Table table = new Table(Collections.singletonList(board));

        assertEquals(producedResource == requiredResource, requirements.areMetWithoutNeighboursBy(board));
        assertEquals(producedResource == requiredResource,
                requirements.areMetWithHelpBy(board, Collections.emptyList()));

        if (producedResource == requiredResource) {
            assertTrue(requirements.couldBeMetBy(table, 0));
        }
    }

    @Theory
    public void resourceRequirement_boughtResource(ResourceType initialResource, ResourceType boughtResource,
                                                  ResourceType requiredResource) {
        assumeTrue(initialResource != requiredResource);

        Requirements requirements = TestUtils.createRequirements(requiredResource);

        Board board = TestUtils.createBoard(initialResource, 2);
        Board neighbourBoard = TestUtils.createBoard(initialResource, 0);
        neighbourBoard.getPublicProduction().addFixedResource(boughtResource, 1);
        Table table = new Table(Arrays.asList(board, neighbourBoard));

        BoughtResources resources = new BoughtResources();
        resources.setProvider(Provider.RIGHT_PLAYER);
        resources.setResources(TestUtils.createResources(boughtResource));

        assertFalse(requirements.areMetWithoutNeighboursBy(board));
        assertEquals(boughtResource == requiredResource,
                requirements.areMetWithHelpBy(board, Collections.singletonList(resources)));

        if (boughtResource == requiredResource) {
            assertTrue(requirements.couldBeMetBy(table, 0));
        }
    }

    @Theory
    public void pay_boughtResource(ResourceType initialResource, ResourceType requiredResource) {
        assumeTrue(initialResource != requiredResource);

        Requirements requirements = TestUtils.createRequirements(requiredResource);

        Board board = TestUtils.createBoard(initialResource, 2);
        Board neighbourBoard = TestUtils.createBoard(requiredResource, 0);
        Table table = new Table(Arrays.asList(board, neighbourBoard));

        BoughtResources boughtResources = new BoughtResources();
        boughtResources.setProvider(Provider.RIGHT_PLAYER);
        boughtResources.setResources(TestUtils.createResources(requiredResource));

        assertFalse(requirements.areMetWithoutNeighboursBy(board));
        assertTrue(requirements.areMetWithHelpBy(board, Collections.singletonList(boughtResources)));
        assertTrue(requirements.couldBeMetBy(table, 0));

        requirements.pay(table, 0, Collections.singletonList(boughtResources));

        assertEquals(0, board.getGold());
        assertEquals(2, neighbourBoard.getGold());
    }
}
