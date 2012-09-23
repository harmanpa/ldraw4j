package com.sigpwned.ldraw4j.io.command.meta;

import com.sigpwned.ldraw4j.io.command.AbstractLDRAWCommand;
import com.sigpwned.ldraw4j.io.command.MetaCommand;
import com.sigpwned.ldraw4j.io.command.meta.body.AbstractBodyMetaCommand;
import com.sigpwned.ldraw4j.io.command.meta.header.AbstractHeaderMetaCommand;

public abstract class AbstractMetaCommand extends AbstractLDRAWCommand implements MetaCommand {
	public AbstractHeaderMetaCommand asHeader() {
		return (AbstractHeaderMetaCommand) this;
	}

	public AbstractBodyMetaCommand asBody() {
		return (AbstractBodyMetaCommand) this;
	}
	
	public UnofficialMetaCommand asUnofficial() {
		return (UnofficialMetaCommand) this;
	}

	public BFCMetaCommand asBFC() {
		return (BFCMetaCommand) this;
	}
}