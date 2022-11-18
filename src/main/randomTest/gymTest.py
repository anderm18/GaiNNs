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
        