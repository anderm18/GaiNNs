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
        

class GoLeftEnv(Env):
    """
    Custom Environment that follows gym interface.
    This is a simple env where the agent must learn to go always left. 
    """
    # Because of google colab, we cannot implement the GUI ('human' render mode)
    metadata = {'render.modes': ['console']}
    # Define constants for clearer code
    LEFT = 0
    RIGHT = 1

    def __init__(self, grid_size=10):
        super(GoLeftEnv, self).__init__()

        # Size of the 1D-grid
        self.grid_size = grid_size
        # Initialize the agent at the right of the grid
        self.agent_pos = grid_size - 1

        # Define action and observation space
        # They must be gym.spaces objects
        # Example when using discrete actions, we have two: left and right
        n_actions = 2
        self.action_space = spaces.Discrete(n_actions)
        # The observation will be the coordinate of the agent
        # this can be described both by Discrete and Box space
        self.observation_space = spaces.Box(low=0, high=self.grid_size,
                                            shape=(1,), dtype=np.float32)

    def reset(self):
        """
        Important: the observation must be a numpy array
        :return: (np.array) 
        """
        # Initialize the agent at the right of the grid
        self.agent_pos = self.grid_size - 1
        # here we convert to float32 to make it more general (in case we want to use continuous actions)
        return np.array([self.agent_pos]).astype(np.float32)

    def step(self, action):
        if action == self.LEFT:
            self.agent_pos -= 1
        elif action == self.RIGHT:
            self.agent_pos += 1
        else:
            raise ValueError("Received invalid action={} which is not part of the action space".format(action))

        # Account for the boundaries of the grid
        self.agent_pos = np.clip(self.agent_pos, 0, self.grid_size)

        # Are we at the left of the grid?
        done = bool(self.agent_pos == 0)

        # Null reward everywhere except when reaching the goal (left of the grid)
        reward = 1 if self.agent_pos == 0 else 0

        # Optionally we can pass additional info, we are not using that for now
        info = {}

        return np.array([self.agent_pos]).astype(np.float32), reward, done, info

    def render(self, mode='console'):
        if mode != 'console':
            raise NotImplementedError()
        # agent is represented as a cross, rest as a dot
        print("." * self.agent_pos, end="")
        print("x", end="")
        print("." * (self.grid_size - self.agent_pos))

    def close(self):
        pass


def train():
    env = CEnv()
    observation, end = env.reset()
    while not end:
        observation, end = env.step(env.random_action())
        env.render()

from stable_baselines3 import PPO3

