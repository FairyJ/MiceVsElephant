

# Aliases #
alias cls="clear"
alias nuke="rm -rf"
alias e="vim"
alias who="who -uTH"
alias cd2="cd ../.."
alias cd3="cd ../../.."
alias cd4="cd ../../../.."
alias cd5="cd ../../../../.."
alias cd6="cd ../../../../../.."
alias ls="/bin/ls -FG"
alias la="/bin/ls -aFG"
alias ll="/bin/ls -laFhG"
alias cd="newcd"


# Environment Variables


newcd() { 
	builtin cd "$@"; 
	ls; 
}

git_branch() {
	git branch 2> /dev/null | sed -e '/^[^*]/d' -e 's/* \(.*\)/ (\1)/'
}

git_dirty() {
	[[ -n "$(git status -s 2> /dev/null)" ]] && echo "*"
}

PS1='\[\033[31;1m\]\u@\h\[\033[0m\]:<$PWD>\[\033[36m\]$(git_branch)$(git_dirty)\[\033[33;1m\] $ \[\033[0m\]'
# PS1='\[\033[31;1m\]\u@\h\[\033[0m\]:<$PWD>\[\033[33;1m\]$(__git_ps1 "[%s]") $ \[\033[0m\]'
export PS1
