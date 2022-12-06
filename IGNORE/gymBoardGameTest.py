import catan.board
import catan.game
import catan.trading

from gymnasium.spaces import Discrete, MultiDiscrete
from pettingzoo.utils.env import ParallelEnv


class CatanEnv(ParallelEnv):
    def __init__(self):
        pass

    def reset(self, seed=None, return_info=False, options=None):
        pass

    def step(self, actions):
        pass

    def render(self):
        pass

    def observation_space(self, agent):
        return self.observation_spaces[agent]

    def action_space(self, agent):
        return self.action_spaces[agent]
import numpy as np

# [r, p, s]
# [0.7, 0.1, 0.2] ->
# [1, 0, 0]
class CEnv(Env):
    def __init__(self, max_games):
        self.action_space = spaces.Box(low=[0,0,0], high=[1,1,1])
        self.observation_space = spaces.Discrete(low=[0,0], high=[max_games, max_games])
        self.wins = 0
        self.losses = 0
        self.max_games = max_games

    def render(self, mode=None):
        if mode == "text":
            print(self.wins, self.losses)
        

    def calculate_winner(self, actor, bot):
        pass

    def step(self, action): # obs, rewards,  end
        choice = np.argmax(action)
        bot_choice = np.random.randint(0, 3)
        self.calculate_winner(choice, bot_choice)
        return [self.wins, self.losses], self.wins + self.losses >= self.max_games

    def reset(self):
        self.wins = 0
        self.losses = 0
        