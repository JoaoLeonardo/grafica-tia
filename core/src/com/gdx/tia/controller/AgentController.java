package com.gdx.tia.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.gdx.tia.TacticalInfiltrationAction;
import com.gdx.tia.element.Agent;
import com.gdx.tia.element.ShadowCaster;
import com.gdx.tia.element.World;
import com.gdx.tia.enums.Direction;
import com.gdx.tia.processor.AgentProcessor;

public class AgentController implements ActionController {

    public static AgentController ref;

    private ShadowCaster shadowCaster;

    private AgentProcessor agentProcessor;
    private Agent agent;

    public AgentController() {
        agent = new Agent();
        ref = this;
    }

    @Override
    public void create() {
        agentProcessor = new AgentProcessor(this);
        Gdx.input.setInputProcessor(agentProcessor);
        setAgentSprite(Direction.RIGHT.name());
        shadowCaster = new ShadowCaster(getAgentSprite());
    }

    @Override
    public void drawElements(Batch batch) {
        // atualiza a posição do agente
        agentProcessor.update();

        // desenha o frame do agente
        if (!agent.hasBeenHit) {
            agent.sprite.draw(batch);
            shadowCaster.update(getAgentSprite(), agentProcessor.getMovementDirection());
        } else {
            agent.hasBeenHit = false;
        }

        // evoca a fala (nem sempre é exibida)
        agent.speak(batch, agentProcessor.position);
    }

    public Agent getAgent() { return agent; }

    public Sprite getAgentSprite() { return agent.sprite; }

    public void setAgentSprite(String region) { agent.setSprite(region, agentProcessor.position); }

    public void dispose() {
        shadowCaster.remove();
        agent.dispose();
    }

}
